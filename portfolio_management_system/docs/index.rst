.. image:: ../media/logo_v1-grey.png
   :scale: 40 %
   :align: center

.. raw:: html

    <embed>
        <p align="center">
            <a href="https://www.python.org/">
                <img src="https://img.shields.io/badge/python-v3-brightgreen.svg?style=flat-square"
                    alt="python"></a> &nbsp;
            <a href="https://pypi.org/project/PyPortfolioOpt/">
                <img src="https://img.shields.io/badge/pypi-v0.2.1-brightgreen.svg?style=flat-square"
                    alt="python"></a> &nbsp;
            <a href="https://opensource.org/licenses/MIT">
                <img src="https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat-square"
                    alt="MIT license"></a> &nbsp;
            <a href="https://github.com/robertmartin8/PyPortfolioOpt/graphs/commit-activity">
                <img src="https://img.shields.io/badge/Maintained%3F-yes-brightgreen.svg?style=flat-square"
                    alt="MIT license"></a> &nbsp;
        </p>
    </embed>


PyPortfolioOpt is a library that implements portfolio optimisation methods, including
classical efficient frontier techniques as well as recent developments in the field
like shrinkage and CVaR optimisation, along with some novel experimental features.
It is **extensive** yet easily
**extensible**, and can be useful for both the casual investor and the serious
practitioner. Whether you are a fundamentals-oriented investor who has identified a
handful of undervalued picks, or an algorithmic trader who has a basket of
interesting signals, PyPortfolioOpt can help you combine your alpha-generators
in a risk-efficient way.


Installation
============

Installation should *never* be more difficult than::

    pip install PyPortfolioOpt

The alternative is to clone/download the project, then in the project directory run

.. code-block:: text

    python setup.py install

.. note::
    If either of these methods doesn't work, please `raise an issue
    <https://github.com/robertmartin8/PyPortfolioOpt/issues>`_  on GitHub


For developers
--------------

If you are planning on using PyPortfolioOpt as a starting template for significant
modifications, it probably makes sense to clone this repository and to just use the
source code

.. code-block:: text

    git clone https://github.com/robertmartin8/PyPortfolioOpt

Alternatively, if you still want the convenience of ``from pypfopt import x``,
you should try

.. code-block:: text

    pip install -e git+https://github.com/robertmartin8/PyPortfolioOpt.git


A Quick Example
===============

This section contains a quick look at what PyPortfolioOpt can do. For a full tour,
please check out the :ref:`user-guide`.

If you already have expected returns ``mu`` and a risk model ``S`` for your set of
assets, generating an optimal portfolio is as easy as:

.. code:: python

    from pypfopt.efficient_frontier import EfficientFrontier

    ef = EfficientFrontier(mu, S)
    weights = ef.max_sharpe()

However, if you would like to use PyPortfolioOpt's built-in methods for
calculating the expected returns and covariance matrix from historical data,
that's fine too.

.. code:: python

    import pandas as pd
    from pypfopt.efficient_frontier import EfficientFrontier
    from pypfopt import risk_models
    from pypfopt import expected_returns

    # Read in price data
    df = pd.read_csv("tests/stock_prices.csv", parse_dates=True, index_col="date")

    # Calculate expected returns and sample covariance
    mu = expected_returns.mean_historical_return(df)
    S = risk_models.sample_cov(df)

    # Optimise for maximal Sharpe ratio
    ef = EfficientFrontier(mu, S)
    weights = ef.max_sharpe()
    ef.portfolio_performance(verbose=True)

This outputs the following:

.. code-block:: text

   Expected annual return: 33.0%
   Annual volatility: 21.7%
   Sharpe Ratio: 1.43


Contents
========

.. toctree::
    :maxdepth: 2

    UserGuide
    ExpectedReturns
    RiskModels
    EfficientFrontier
    OtherOptimisers
    Postprocessing
    Roadmap
    Contributing
    About

Advantages over existing implementations
========================================

- Includes both classical methods (Markowitz 1952), suggested best practices
  (e.g covariance shrinkage), along with many recent developments and novel
  features, like L2 regularisation, shrunk covariance, hierarchical risk parity.
- Native support for pandas dataframes: easily input your daily prices data.
- Extensive practical tests, which use real-life data.
- Easy to combine with your own proprietary strategies and models.
- Robust to missing data, and price-series of different lengths (e.g FB data
  only goes back to 2012 whereas AAPL data goes back to 1980).


Project principles and design decisions
=======================================

- It should be easy to swap out individual components of the optimisation process
  with the user's proprietary improvements.
- Usability is everything: it is better to be self-explanatory than consistent.
- There is no point in portfolio optimisation unless it can be practically
  applied to real asset prices.
- Everything that has been implemented should be tested.
- Inline documentation is good: dedicated (separate) documentation is better.
  The two are not mutually exclusive.
- Formatting should never get in the way of good code: because of this,
  I have deferred **all** formatting decisions to `Black
  <https://github.com/ambv/black>`_.




Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`
