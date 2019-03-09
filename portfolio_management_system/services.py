import repo


def max_gainer_loser():
    portfolio_repo = repo.PortfolioRepo()
    users_repo = repo.UserRepo()

    query = {}
    projection = {"email_id": 1}
    users_data = list(users_repo.find_record_with_projection(query, projection))
    print(users_data)
    query = {"email_id": ""}
    projection = {"cost_price": 1, "current_qty": 1, "current_price": 1, "symbol": 1, "original_qty": 1, "original_price": 1}

    for user_id in users_data:

        maximum_gain = -9999999
        maximum_loss = 9999999
        max_gainer = ""
        max_loser = ""
        total_investment = 0
        unrealised_difference = 0
        original_investment = 0

        query["email_id"] = user_id["email_id"]
        profile_status = list(portfolio_repo.find_record_with_projection(query, projection))

        if profile_status:
            for status_updates in profile_status:
                current_investment = status_updates["cost_price"] * status_updates["current_qty"]
                current_valuation = status_updates["current_price"] * status_updates["current_qty"]
                original_investment += status_updates["cost_price"] * status_updates["original_qty"]

                difference = current_valuation - current_investment
                total_investment += current_investment
                unrealised_difference += difference

                if difference > maximum_gain:
                    maximum_gain = difference
                    max_gainer = status_updates["symbol"]

                if difference < maximum_loss:
                    maximum_loss = difference
                    max_loser = status_updates["symbol"]

            update = {"max_gainer_symbol": max_gainer, "max_gainer_amt": maximum_gain, "max_loser_symbol": max_loser,
                      "max_loser_amt": maximum_loss, "total_investment": total_investment,
                      "unrealised_difference": unrealised_difference,
                      "balance": original_investment - current_investment}
            users_repo.insert_record({"email_id": user_id["email_id"]}, {'$set': update})
        else:
            continue

    return "Success"


def get_chart_data(email_id):
    portfolio_repo = repo.PortfolioRepo()
    users_repo = repo.UserRepo()
    chart_data = {'x': [], 'y': []}

    query = {"email_id": ""}
    projection = {"cost_price": 1, "current_qty": 1, "current_price": 1, "symbol": 1, "original_qty": 1}

    return_valuation = {'symbol': [], 'value': []}
    current_valuation = {}
    for user_id in [email_id]:

        query["email_id"] = user_id
        profile_status = list(portfolio_repo.find_record(query))

        if profile_status:

            for status_updates in profile_status:
                current_valuation[status_updates["symbol"]] = 0

            for status_updates in profile_status:
                # current_valuation['symbol'].append(status_updates["symbol"])
                # current_valuation['value'].append(status_updates["current_price"] * status_updates["current_qty"])
                current_valuation[status_updates["symbol"]] += status_updates["current_price"] * status_updates["current_qty"]

            for x in current_valuation:
                return_valuation['symbol'].append(x)
                return_valuation['value'].append(current_valuation[x])
        else:
            continue

    return return_valuation


def run_stochastic():
    print("IN CALL to PARAM")
    portfolio_repo = repo.PortfolioRepo()
    users_repo = repo.UserRepo()
    stocks_repo = repo.StocksRepo()
    ohlc_repo = repo.OhlcRepo()
    stocks_list = []

    records = list(stocks_repo.find_record_with_projection({}, {"symbol": 1}))
    for x in records:
        stocks_list.append(x["symbol"])

    # print (records)
    count = 0
    lowest = 999
    highest = 0
    stoch_list = {}
    ma_list = []
    final_dict = {}
    buy_count = 0
    sell_count = 0

    for symbol in stocks_list:

        query = {}
        projection = {"close": 1}
        latest = list(ohlc_repo.find_record_with_projection_limit(symbol, query, projection, int(1)))
        latest_endpoint = latest[0]['close']
        # print(close)

        lower = []
        higher = []

        query = {}
        projection = {"low": 1}
        low = list(ohlc_repo.find_record_with_projection_limit(symbol, query, projection, int(5)))
        projection = {"high": 1}
        high = list(ohlc_repo.find_record_with_projection_limit(symbol, query, projection, int(5)))

        projection = {"low": 1}
        ma_low = list(ohlc_repo.find_record_with_projection_limit(symbol, query, projection, int(5)))
        projection = {"high": 1}
        ma_high = list(ohlc_repo.find_record_with_projection_limit(symbol, query, projection, int(5)))

        for x in low:
            lower.append(x["low"])
        for x in high:
            higher.append(x["high"])

        lowest = min(lower)
        highest = max(higher)
        stoch = 100 * (latest_endpoint - lowest) / (highest - lowest)

        if stoch >= 90:
            stoch_list[symbol] = "SELL"
            sell_count += 1
        elif stoch <= 10:
            stoch_list[symbol] = "BUY"
            buy_count += 1
        else:
            continue

    print("Buy count: " + str(buy_count))
    print('Sell count: ' + str(sell_count))
    buy_list = []
    for stock in stoch_list:
        if stoch_list[stock] == "BUY":
            buy_list.append(stock)
    return buy_list



