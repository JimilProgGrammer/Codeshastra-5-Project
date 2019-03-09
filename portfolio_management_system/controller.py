from flask import Flask, Response, request
from flask_apscheduler import APScheduler
import logging
import optimize_portfolio
import services
import json


class Config(object):
    SCHEDULER_API_ENABLED = True


app = Flask(__name__)
app.config.from_object(Config())

scheduler = APScheduler()
scheduler.init_app(app)
scheduler.start()


# @scheduler.task('cron', hour=4, minute=5, second=30)
@app.route("/optimize")
def manage_portfolio():
    return create_json(optimize_portfolio.main())


@app.route("/max_gainer_loser")
def max_gainer_loser():
    return create_json(services.max_gainer_loser())


@app.route("/flask/get_chart_data/<email_id>")
def get_chart_data(email_id):
    return create_json(services.get_chart_data(email_id))


@app.route("/run_stochastic")
def run_stochastic():
    return create_json(services.run_stochastic())


def create_json(data):
    """Utility method that creates a json response from the data returned by the service method.
    :param data:
    :return:
    """
    base_response_dto = {
        'data': str(data),
    }
    js = json.dumps(base_response_dto)
    resp = Response(js, status=200, mimetype='application/json')
    return resp


if __name__ == "__main__":
    logging.basicConfig(filename='app.log', level=logging.INFO)
    app.run(debug=True)
