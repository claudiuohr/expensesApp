import json

from flask import Flask, request, jsonify, render_template, redirect, url_for
import requests

app = Flask(__name__)

BASE_URL = 'http://localhost:8080/api'


@app.route('/')
def index():
    return render_template("index.html")


@app.route('/login', methods=['GET'])
def login():
    return render_template("login.html")


@app.route('/signup', methods=['GET'])
def signup():
    return render_template("signup.html")


@app.route('/users')
def showUser():
    user = request.args.get('user')
    if user:
        user = json.loads(user)
    return render_template('users.html', user=user)


@app.route('/createAccount', methods=['POST'])
def createAccount():
    fname = request.form['firstName']
    lname = request.form['lastName']
    uname = request.form['username']
    p1 = request.form['password1']
    p2 = request.form['password2']

    if p1 != p2:
        return "Passwords do not match", 400

    data = {
        'firstName': fname,
        'lastName': lname,
        'username': uname,
        'password': p1,
    }
    response = requests.post(f'{BASE_URL}/users/addUser', json=data)
    if response.status_code != 201:
        return f"Error creating account: {response.text}", response.status_code
    return render_template('login.html')


@app.route("/addExpense", methods=['Post'])
def addExpense():
    uname = request.form['username']
    category = request.form['category']
    amount = float(request.form['amount'])
    data = {
        'username': uname,
        'category': category,
        'amount': amount
    }
    response = requests.post(f'{BASE_URL}/users/addExpense', json=data)

    if response.status_code != 200:
        return f"Error adding expense: {response.text}", response.status_code
    user = response.json()
    user = json.dumps(user)
    return redirect(url_for('showUser', user=user))


@app.route('/get_user', methods=['POST'])
def get_user():
    username = request.form['username']
    password = request.form['password']

    response = requests.get(f'{BASE_URL}/users/{username}', params={'password': password})

    if response.status_code == 200:
        user = response.json()
        user = json.dumps(user)
        return redirect(url_for("showUser", user=user))
    elif response.status_code == 404:
        return render_template("login.html", error="User not found or incorrect password.")
    else:
        return render_template("login.html", error="An error occurred.")


@app.route('/update_user', methods=['POST'])
def update_user():
    username = request.form['username']
    fname = request.form['fname']
    lname = request.form['lname']

    response = requests.patch(f'{BASE_URL}/users/updateCredentials/{username}', params={'fname': fname, 'lname': lname})

    if response.status_code == 200:
        user = response.json()
        user = json.dumps(user)
        return redirect(url_for("showUser", user=user))
    elif response.status_code == 404:
        return render_template("login.html", error="User not found or incorrect password.")
    else:
        return render_template("login.html", error="An error occurred.")


@app.route('/update_password', methods=['POST'])
def update_password():
    username = request.form['username']
    oldp = request.form['oldp']
    newp = request.form['newp']

    response = requests.patch(f'{BASE_URL}/users/updatePassword/{username}', params={'oldp': oldp, 'newp': newp})

    if response.status_code == 200:
        user = response.json()
        user = json.dumps(user)
        return redirect(url_for("showUser", user=user))
    elif response.status_code == 404:
        return render_template("login.html", error="User not found or incorrect password.")
    else:
        return render_template("login.html", error="An error occurred.")


@app.route('/delete_user', methods=['POST'])
def delete_user():
    username = request.form['username']
    password = request.form['password']

    response = requests.delete(f'{BASE_URL}/users/deleteUser/{username}', params={'password': password})
    if response.status_code == 200:
        return render_template("login.html")
    elif response.status_code == 404:
        user = response.json()
        user = json.dumps(user)
        return redirect(url_for("showUser", user=user))
    else:
        return render_template("login.html", error="An error occurred.")

