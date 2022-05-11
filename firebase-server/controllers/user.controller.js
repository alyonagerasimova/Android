const express = require("express");
const e = require("express");
const { getAuth } = require("firebase-admin/auth");

class UserController {
    _router = new express.Router();
    apiURL;
    firebaseApp;
    welcome = 'Welcome';

    constructor(apiURL, firebaseApp) {
        this.apiURL = apiURL;
        this.firebaseApp = firebaseApp;

        this.registerGETWelcome();
        this.registerPOSTWelcome();
        this.registerGETRandom();
    }

    get router() {
        return this._router;
    }

    registerGETRandom() {
        this._router.get(this.apiURL + "/user/random", (request, response) => {
            response.status(200).json(Math.random())
        });
    }

    registerGETWelcome() {
        this._router.get(this.apiURL + "/user/welcome", (request, response) => {
            const idToken = request.headers['x-idtoken'];
            console.log(request.headers);
            if (idToken) {
                this.verifyIdToken(request).then(decodedToken => {
                    const name = decodedToken.email;
                    response.status(200).json({
                        welcome: this.welcome + ', ' + name,
                    })
                })
                    .catch((error) => {
                        console.log('error', error);
                        response.status(500).json({
                            welcome: this.welcome,
                            updated: false,
                            message: 'Erorr token! ' + error.message
                        })
                    });
            } else {
                console.log('500', 500);
                response.status(500).json({
                    welcome: this.welcome,
                    updated: false,
                    message: 'No token!'
                })
            }
        });
    }

    registerPOSTWelcome() {
        this._router.post(this.apiURL + "/user/welcome", (request, response) => {
            if (request.query.message) {
                this.welcome = request.query.message;
                response.status(200).json({
                    welcome: this.welcome,
                    updated: true,
                })
            } else {
                response.status(500).json({
                    welcome: this.welcome,
                    updated: false,
                    message: 'No welcome text!'
                })
            }
        });
    }

    verifyIdToken(request) {
        const uuid = request.headers['x-userid'];
        const idToken = request.headers['x-idtoken'];

        return getAuth()
            .verifyIdToken(idToken)
            .then((decodedToken) => {
                const uid = decodedToken.uid;
                if (uid != uuid) {
                    throw Error('error X-UserId');
                }
                console.log('Parsed: ', decodedToken);

                return decodedToken;
            })
    }
}

module.exports = UserController;
