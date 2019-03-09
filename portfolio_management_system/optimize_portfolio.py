import pandas as pd
import numpy as np
from pypfopt.efficient_frontier import EfficientFrontier
from pypfopt import risk_models
from pypfopt import expected_returns
from pypfopt.hierarchical_risk_parity import hrp_portfolio
from pypfopt import discrete_allocation

from pymongo import *
import repo
import services


def load_data():
    # Read in price data
    # df = pd.read_csv("tests/stock_prices.csv", parse_dates=True, index_col="date")
    mng_client = MongoClient('localhost', 27017)
    db = mng_client.portfolio_manager

    # stocks_list = ["RELIANCE", "HDFCBANK", "SUNPHARMA", "DRREDDY", "RALLIS", "MRF"]
    stocks_list = services.run_stochastic()
    full_dates = []
    date_list = list(db.RELIANCE.find({}, {"timestamp": 1}).sort("timestamp", DESCENDING))
    for x in date_list:
        full_dates.append(str(x['timestamp']))

    dataframe_columns = ['Date']
    count = 0
    for x in stocks_list:
        if x not in ['stocks', 'config'] and count < 30:
            dataframe_columns.append(x)
            count += 1

    df = pd.DataFrame(columns=dataframe_columns)
    df["Date"] = full_dates
    df.set_index('Date', inplace=True)

    for x in dataframe_columns:
        if x not in ['stocks', 'config']:
            stock_data = list(db[x].find())
            for data_point in stock_data:
                df.at[str(data_point["timestamp"]), x] = data_point["close"]
                # df.set_value(str(data_point["timestamp"]), str(x), data_point["close"])

    df = df.sort_values(by=['Date'], ascending=False)
    # df = df.head(252)
    print(df.columns.values)
    new_df = df.sort_values(by=['Date'], ascending=True)
    print(new_df)
    new_df.fillna(new_df.mean())
    return new_df


def main():

    mng_client = MongoClient('localhost', 27017)
    db = mng_client.portfolio_manager

    portfolio_repo = repo.PortfolioRepo()
    users_repo = repo.UserRepo()

    query = {}
    projection = {"email_id": 1, "risk_factor": 1, "budget": 1, "target_return": 1}
    users_data = list(users_repo.find_record_with_projection(query, projection))

    for user_id in users_data:
        email_id = user_id["email_id"]
        optimization_dict = {}
        portfolio_dict = {"portfolio": []}

        optimization_dict["date"] = "2019-03-01"

        df = load_data()
        returns = df.pct_change().dropna(how="all")
        print("Printing returns:\n")
        print(df)
        # Mean historical returns: Average price * 252
        mu = expected_returns.mean_historical_return(df)

        # Covariance means how two variables relate to each other
        # A portfolio manager will look for stocks with negative covariance
        # which minimizes risk as if one stock price goes down
        # , other will go up.
        S = risk_models.sample_cov(df)

        # Long-only Maximum Sharpe portfolio, with discretised weights
        ef = EfficientFrontier(mu, S)
        # Higher the volatility, more the risk
        # What returns you receive for the extra volatility
        weights = ef.max_sharpe()
        annual_return, volatility, sharpe = ef.portfolio_performance(verbose=True)

        optimization_dict["long_only_max_sharpe"] = {}
        optimization_dict["long_only_max_sharpe"]["annual_return"] = annual_return*100
        optimization_dict["long_only_max_sharpe"]["volatility"] = volatility*100
        optimization_dict["long_only_max_sharpe"]["sharpe_ratio"] = sharpe

        latest_prices = discrete_allocation.get_latest_prices(df)
        allocation, leftover = discrete_allocation.portfolio(weights, latest_prices, total_portfolio_value=user_id["budget"])
        print("\nFinding which stocks are to be added to our portfolio:\n")
        print("Discrete allocation:", allocation)
        print("Funds remaining: ${:.2f}\n".format(leftover))
        optimization_dict["balance"] = leftover

        """for suggested_stock in allocation:
            individual_stock["symbol"] = suggested_stock"""

        total_investment = 0
        individual_investment = {}
        for filtered_stock in allocation:
            individual_stock = {}
            current_price = db[filtered_stock].find({}, {"close": 1}).sort("timestamp", DESCENDING).limit(1)[0]["close"]
            individual_stock["current_price"] = current_price
            individual_stock["symbol"] = filtered_stock
            individual_stock["qty"] = allocation[filtered_stock]
            individual_stock["amount"] = current_price*allocation[filtered_stock]
            total_investment += current_price*allocation[filtered_stock]
            individual_investment[filtered_stock] = current_price*allocation[filtered_stock]
            portfolio_dict["portfolio"].append(individual_stock)

        print("\nPortfolio Dictionary: \n")
        print(portfolio_dict)
        print("Total investment: " + str(total_investment))
        optimization_dict["total_value"] = total_investment
        print("Individual investment: ")
        print(individual_investment)

        """
        Expected annual return: 33.0%
        Annual volatility: 21.7%
        Sharpe Ratio: 1.43
        
        Discrete allocation: {'MA': 14, 'FB': 12, 'PFE': 51, 'BABA': 5, 'AAPL': 5,
                              'AMZN': 0, 'BBY': 9, 'SBUX': 6, 'GOOG': 1}
        Funds remaining: $12.15
        """

        # Long-only minimum volatility portfolio, with a weight cap and regularisation
        # e.g if we want at least 15/20 tickers to have non-neglible weights, and no
        # asset should have a weight
        # greater than 10%
        print("\nLONG ONLY with minimum volatility with weight bounds 0 to 10: \n")
        ef = EfficientFrontier(mu, S, weight_bounds=(0, 0.10), gamma=1)
        weights = ef.min_volatility()
        print(weights)
        annual_return, volatility, sharpe = ef.portfolio_performance(verbose=True)

        optimization_dict["long_only_min_volatility"] = {}
        optimization_dict["long_only_min_volatility"]["annual_return"] = annual_return*100
        optimization_dict["long_only_min_volatility"]["volatility"] = volatility*100
        optimization_dict["long_only_min_volatility"]["sharpe_ratio"] = sharpe

        """
        {
            "GOOG": 0.07350956640872872,
            "AAPL": 0.030014017863649482,
            "FB": 0.1,
            "BABA": 0.1,
            "AMZN": 0.020555866446753328,
            "GE": 0.04052056082259943,
            "AMD": 0.00812443078787937,
            "WMT": 0.06506870608367901,
            "BAC": 0.008164561664321555,
            "GM": 0.1,
            "T": 0.06581732376444831,
            "UAA": 0.04764331094366604,
            "SHLD": 0.04233556511047908,
            "XOM": 0.06445358180591973,
            "RRC": 0.0313848213281047,
            "BBY": 0.02218378020003044,
            "MA": 0.068553464907087,
            "PFE": 0.059025401478094965,
            "JPM": 0.015529411963789761,
            "SBUX": 0.03711562842076907,
        }
        
        Expected annual return: 22.7%
        Annual volatility: 12.7%
        Sharpe Ratio: 1.63
        """

        # A long/short portfolio maximising return for a target volatility of 10%,
        # with a shrunk covariance matrix risk model
        print("\nLONG | SHORT with target volatility of 10%: \n")
        shrink = risk_models.CovarianceShrinkage(df)
        S = shrink.ledoit_wolf()
        ef = EfficientFrontier(mu, S, weight_bounds=(-1, 1))
        weights = ef.efficient_risk(target_risk=float(user_id["risk_factor"]))
        annual_return, volatility, sharpe = ef.portfolio_performance(verbose=True)

        optimization_dict["long_short_target"] = {}
        optimization_dict["long_short_target"]["annual_return"] = annual_return*100
        optimization_dict["long_short_target"]["volatility"] = volatility*100
        optimization_dict["long_short_target"]["sharpe_ratio"] = sharpe

        """
        Expected annual return: 29.8%
        Annual volatility: 10.0%
        Sharpe Ratio: 2.77
        """

        # A market-neutral Markowitz portfolio finding the minimum volatility
        # for a target return of 20%
        print("\nCalculating Markowitz portfolio: Target return of 20%\n")
        print("Maximize returns at minimum risk by diversifying our stock investments:\n")
        ef = EfficientFrontier(mu, S, weight_bounds=(-1, 1))
        weights = ef.efficient_return(target_return=float(user_id["target_return"]), market_neutral=True)
        annual_return, volatility, sharpe = ef.portfolio_performance(verbose=True)

        optimization_dict["mpt"] = {}
        optimization_dict["mpt"]["annual_return"] = annual_return*100
        optimization_dict["mpt"]["volatility"] = volatility*100
        optimization_dict["mpt"]["sharpe_ratio"] = sharpe

        """
        Expected annual return: 20.0%
        Annual volatility: 16.5%
        Sharpe Ratio: 1.09
        """

        # Custom objective
        def utility_obj(weights, mu, cov_matrix, k=1):
            return -weights.dot(mu) + k * np.dot(weights.T, np.dot(cov_matrix, weights))

        ef = EfficientFrontier(mu, S)
        ef.custom_objective(utility_obj, ef.expected_returns, ef.cov_matrix, 1)
        annual_return, volatility, sharpe = ef.portfolio_performance(verbose=True)

        optimization_dict["custom_obj"] = {}
        optimization_dict["custom_obj"]["annual_return"] = annual_return*100
        optimization_dict["custom_obj"]["volatility"] = volatility*100
        optimization_dict["custom_obj"]["sharpe_ratio"] = sharpe

        """
        Expected annual return: 40.1%
        Annual volatility: 29.2%
        Sharpe Ratio: 1.30
        """

        ef.custom_objective(utility_obj, ef.expected_returns, ef.cov_matrix, 2)
        annual_return, volatility, sharpe = ef.portfolio_performance(verbose=True)

        optimization_dict["custom_obj_extended"] = {}
        optimization_dict["custom_obj_extended"]["annual_return"] = annual_return*100
        optimization_dict["custom_obj_extended"]["volatility"] = volatility*100
        optimization_dict["custom_obj_extended"]["sharpe_ratio"] = sharpe

        """
        Expected annual return: 36.6%
        Annual volatility: 24.7%
        Sharpe Ratio: 1.39
        """

        # CVaR optimisation
        """vr = CVAROpt(returns)
        vr.min_cvar()
        print(vr.clean_weights())"""

        """
        {'GOOG': 0.10886,
         'AAPL': 0.0,
         'FB': 0.02598,
         'BABA': 0.57691,
         'AMZN': 0.0,
         'GE': 0.01049,
         'AMD': 0.0138,
         'WMT': 0.01581,
         'BAC': 0.01049,
         'GM': 0.03463,
         'T': 0.01049,
         'UAA': 0.07782,
         'SHLD': 0.04184,
         'XOM': 0.00931,
         'RRC': 0.0,
         'BBY': 0.01748,
         'MA': 0.03782,
         'PFE': 0.0,
         'JPM': 0.0,
         'SBUX': 0.00828}
         """

        # Hierarchical risk parity
        print("\nHierarchial Risk Parity: computes a hierarchical tree (using a standard hierarchical clustering algorithm)"
              "from the correlation matrix, and then diversifies accross the different clusters.\n")
        weights = hrp_portfolio(returns)
        print(weights)

        """
        {'AAPL': 0.022258941278778397,
         'AMD': 0.02229402179669211,
         'AMZN': 0.016086842079875,
         'BABA': 0.07963382071794091,
         'BAC': 0.014409222455552262,
         'BBY': 0.0340641943824504,
         'FB': 0.06272994714663534,
         'GE': 0.05519063444162849,
         'GM': 0.05557666024185722,
         'GOOG': 0.049560084289929286,
         'JPM': 0.017675709092515708,
         'MA': 0.03812737349732021,
         'PFE': 0.07786528342813454,
         'RRC': 0.03161528695094597,
         'SBUX': 0.039844436656239136,
         'SHLD': 0.027113184241298865,
         'T': 0.11138956508836476,
         'UAA': 0.02711590957075009,
         'WMT': 0.10569551148587905,
         'XOM': 0.11175337115721229}
        """

        optimization_dict["portfolio"] = portfolio_dict["portfolio"]
        optimization_dict["email_id"] = user_id["email_id"]
        print("\n")
        print(optimization_dict)
        db.notification.update({"email_id": user_id["email_id"]}, {'$set': optimization_dict}, upsert=True)
        return "Success"


if __name__ == "__main__":
    main()