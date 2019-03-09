import warnings
import pandas as pd
import numpy as np
from pypfopt import risk_models
from tests.utilities_for_tests import get_data


def test_sample_cov_dummy():
    data = pd.DataFrame(
        [
            [4.0, 2.0, 0.6],
            [4.2, 2.1, 0.59],
            [3.9, 2.0, 0.58],
            [4.3, 2.1, 0.62],
            [4.1, 2.2, 0.63],
        ]
    )
    test_answer = pd.DataFrame(
        [
            [0.006661687937656102, 0.00264970955585574, 0.0020849735375206195],
            [0.00264970955585574, 0.0023450491307634215, 0.00096770864287974],
            [0.0020849735375206195, 0.00096770864287974, 0.0016396416271856837],
        ]
    )
    S = risk_models.sample_cov(data) / 252
    pd.testing.assert_frame_equal(S, test_answer)


def test_sample_cov_real_data():
    df = get_data()
    S = risk_models.sample_cov(df)
    assert S.shape == (20, 20)
    assert S.index.equals(df.columns)
    assert S.index.equals(S.columns)
    assert S.notnull().all().all()


def test_sample_cov_type_warning():
    df = get_data()
    cov_from_df = risk_models.sample_cov(df)

    returns_as_array = np.array(df)
    with warnings.catch_warnings(record=True) as w:
        cov_from_array = risk_models.sample_cov(returns_as_array)

        assert len(w) == 1
        assert issubclass(w[0].category, RuntimeWarning)
        assert str(w[0].message) == "prices are not in a dataframe"

    np.testing.assert_array_almost_equal(
        cov_from_df.values, cov_from_array.values, decimal=6
    )


def test_sample_cov_frequency():
    df = get_data()
    S = risk_models.sample_cov(df)
    S2 = risk_models.sample_cov(df, frequency=2)
    pd.testing.assert_frame_equal(S / 126, S2)


def test_semicovariance():
    df = get_data()
    S = risk_models.semicovariance(df)
    assert S.shape == (20, 20)
    assert S.index.equals(df.columns)
    assert S.index.equals(S.columns)
    assert S.notnull().all().all()
    S2 = risk_models.semicovariance(df, frequency=2)
    pd.testing.assert_frame_equal(S / 126, S2)


def test_semicovariance_benchmark():
    df = get_data()
    # When the benchmark is very negative, the cov matrix should be zeroes
    S_negative_benchmark = risk_models.semicovariance(df, benchmark=-0.5)
    np.testing.assert_allclose(S_negative_benchmark, 0, atol=1e-4)

    # Increasing the benchmark should increase covariances on average
    S = risk_models.semicovariance(df, benchmark=0)
    S2 = risk_models.semicovariance(df, benchmark=1)
    assert S2.sum().sum() > S.sum().sum()


def test_exp_cov_matrix():
    df = get_data()
    S = risk_models.exp_cov(df)
    assert S.shape == (20, 20)
    assert S.index.equals(df.columns)
    assert S.index.equals(S.columns)
    assert S.notnull().all().all()
    S2 = risk_models.exp_cov(df, frequency=2)
    pd.testing.assert_frame_equal(S / 126, S2)


def test_exp_cov_limits():
    df = get_data()
    sample_cov = risk_models.sample_cov(df)
    S = risk_models.exp_cov(df)
    assert not np.allclose(sample_cov, S)

    # As span gets larger, it should tend towards sample covariance
    S2 = risk_models.exp_cov(df, span=1e20)
    assert np.abs(S2 - sample_cov).max().max() < 1e-3


def test_min_cov_det():
    df = get_data()
    S = risk_models.min_cov_determinant(df, random_state=8)
    assert S.shape == (20, 20)
    assert S.index.equals(df.columns)
    assert S.index.equals(S.columns)
    assert S.notnull().all().all()
    S2 = risk_models.min_cov_determinant(df, frequency=2, random_state=8)
    pd.testing.assert_frame_equal(S / 126, S2)


def test_covariance_shrinkage_init():
    df = get_data()
    cs = risk_models.CovarianceShrinkage(df)
    assert cs.S.shape == (20, 20)
    assert not (np.isnan(cs.S)).any()


def test_shrunk_covariance():
    df = get_data()
    cs = risk_models.CovarianceShrinkage(df)
    shrunk_cov = cs.shrunk_covariance(0.2)
    assert cs.delta == 0.2
    assert shrunk_cov.shape == (20, 20)
    assert list(shrunk_cov.index) == list(df.columns)
    assert list(shrunk_cov.columns) == list(df.columns)
    assert not shrunk_cov.isnull().any().any()


def test_shrunk_covariance_extreme_delta():
    df = get_data()
    cs = risk_models.CovarianceShrinkage(df)
    # if delta = 0, no shrinkage occurs
    shrunk_cov = cs.shrunk_covariance(0)
    np.testing.assert_array_almost_equal(
        shrunk_cov.values, risk_models.sample_cov(df))
    # if delta = 1, sample cov does not contribute to shrunk cov
    shrunk_cov = cs.shrunk_covariance(1)
    N = df.shape[1]
    F = np.identity(N) * np.trace(cs.S) / N
    np.testing.assert_array_almost_equal(shrunk_cov.values, F * 252)


def test_shrunk_covariance_frequency():
    df = get_data()
    cs = risk_models.CovarianceShrinkage(df, frequency=52)
    # if delta = 0, no shrinkage occurs
    shrunk_cov = cs.shrunk_covariance(0)

    S = risk_models.sample_cov(df, frequency=52)
    np.testing.assert_array_almost_equal(shrunk_cov.values, S)


def test_ledoit_wolf():
    df = get_data()
    cs = risk_models.CovarianceShrinkage(df)
    shrunk_cov = cs.ledoit_wolf()
    assert 0 < cs.delta < 1
    assert shrunk_cov.shape == (20, 20)
    assert list(shrunk_cov.index) == list(df.columns)
    assert list(shrunk_cov.columns) == list(df.columns)
    assert not shrunk_cov.isnull().any().any()


def test_oracle_approximating():
    df = get_data()
    cs = risk_models.CovarianceShrinkage(df)
    shrunk_cov = cs.oracle_approximating()
    assert 0 < cs.delta < 1
    assert shrunk_cov.shape == (20, 20)
    assert list(shrunk_cov.index) == list(df.columns)
    assert list(shrunk_cov.columns) == list(df.columns)
    assert not shrunk_cov.isnull().any().any()
