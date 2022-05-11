const express = require("express");

const UserController = require("./controllers/user.controller")
const admin = require("firebase-admin");
const serviceAccount = require("./assets/firebase-adminsdk-key.json");

// const swagger = require('swagger-ui-dist');
// const pathToSwaggerUi = swagger.absolutePath();

const app = express();
const port = 6070;

app.use(express.json());
app.use(express.static("public"));
// app.use(express.static(pathToSwaggerUi))

const firebaseApp = admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://mobile-app-347309-default-rtdb.firebaseio.com"
});

app.use(new UserController("/api", firebaseApp).router);

app.listen(port, function () {
    console.log(`App listening on port ${port}! [http://localhost:${port}/]`, firebaseApp.name);
});