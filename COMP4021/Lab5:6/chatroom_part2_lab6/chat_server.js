const express = require("express");

const bcrypt = require("bcrypt");
const fs = require("fs");
const session = require("express-session");
//const { use } = require("express/lib/application");
//const { error } = require("console");
//let typingtimer;

// Create the Express app
const app = express();

// Use the 'public' folder to serve static files
app.use(express.static("public"));

// Use the json middleware to parse JSON data
app.use(express.json());

// Use the session middleware to maintain sessions
const chatSession = session({
    secret: "game",
    resave: false,
    saveUninitialized: false,
    rolling: true,
    cookie: { maxAge: 300000 }
});
app.use(chatSession);

// This helper function checks whether the text only contains word characters
function containWordCharsOnly(text) {
    return /^\w+$/.test(text);
}

// Handle the /register endpoint
app.post("/register", (req, res) => {
    // Get the JSON data from the body
    const { username, avatar, name, password } = req.body;

    //
    // D. Reading the users.json file
    //
    const users = JSON.parse(fs.readFileSync("./data/users.json"));
    //console.log(users)
    //
    // E. Checking for the user data correctness
    //
    if(!username){
        res.json({
            status: "error",
            error: "username cannot be empty!"
        })
        return;
    }
    if(!avatar){
        res.json({
            status: "error",
            error: "avatar cannot be empty!"
        })
        return;
    }
    if(!name){
        res.json({
            status: "error",
            error: "name cannot be empty!"
        })
        return;
    }
    if(!password){
        res.json({
            status: "error",
            error: "password cannot be empty!"
        })
        return;
    }
    
    if(!containWordCharsOnly(username)){
        res.json({
            status: "error",
            error: "username should only contain chars!"
        })
        return;
    }

    if(username in users){
        res.json({
            status: "error",
            error: "username has already been used!"
        })
        return;
    }
    //
    // G. Adding the new user account
    //
    const hash = bcrypt.hashSync(password, 10);
    users[username] = {avatar, name, hash};
    //
    // H. Saving the users.json file
    //
    fs.writeFileSync("./data/users.json", JSON.stringify(users, null, " "));
    //
    // I. Sending a success response to the browser
    //
    res.json({status: "success"});
    // Delete when appropriate
    //res.json({ status: "error", error: "This endpoint is not yet implemented." });
});

// Handle the /signin endpoint
app.post("/signin", (req, res) => {
    // Get the JSON data from the body
    const { username, password } = req.body;

    //
    // D. Reading the users.json file
    //
    const users = JSON.parse(fs.readFileSync("./data/users.json"));
    //
    // E. Checking for username/password
    //
    if (!(username in users) || !bcrypt.compareSync(password, users[username].hash)) {
        return res.json({
            status: "error",
            error: "incorrect username/password."
        });
    }
    
    //
    // G. Sending a success response with the user account
    //
    const avatar = users[username].avatar;
    const name = users[username].name
    //const user = {username, avatar, name};
    req.session.user = {username, avatar, name};;
    res.json({ 
        status: "success", 
        user: {username, avatar, name}
    });
    // Delete when appropriate
    //res.json({ status: "error", error: "This endpoint is not yet implemented." });
});

// Handle the /validate endpoint
app.get("/validate", (req, res) => {

    //
    // B. Getting req.session.user
    //
    if(req.session.user){
        const {username, avatar, name} = req.session.user;
        res.json({ 
            status: "success", 
            user: {username, avatar, name}
        });
    }else{
        res.json({ 
            status: "error", 
            error: "session not exist."
        });
    }
    //
    // D. Sending a success response with the user account
    //
    
    // Delete when appropriate
    //res.json({ status: "error", error: "This endpoint is not yet implemented." });
});

// Handle the /signout endpoint
app.get("/signout", (req, res) => {

    //
    // Deleting req.session.user
    //
    delete req.session.user;
    //
    // Sending a success response
    //
    res.json({status: "success"});
    // Delete when appropriate
    //res.json({ status: "error", error: "This endpoint is not yet implemented." });
});


//
// ***** Please insert your Lab 6 code here *****
//
const { createServer } = require("http");
const { Server } = require("socket.io");
const { Socket } = require("dgram");
const httpServer = createServer( app );
const io = new Server(httpServer);

io.use((Socket, next) => {
    chatSession(Socket.request, {}, next);
});

const onlineUserList = {};

io.on("connection", (socket) => {
    if(socket.request.session.user){
        const user = socket.request.session.user;
        const {username, avatar, name} = user;
        onlineUserList[username] = user;
        io.emit("add user", JSON.stringify(user));
    }
    socket.on("disconnect", () => {
        if(socket.request.session.user){
            const user = socket.request.session.user;
            const {username, avatar, name} = user;
            if(onlineUserList[username]){
                delete onlineUserList[username];
                //console.log(onlineUserList); 
            }
            io.emit("remove user", JSON.stringify(user));
        }
    });

    socket.on("get users", () => {
        socket.emit("users", JSON.stringify(onlineUserList));
    });

    socket.on("get messages", () => {
    // Send the chatroom messages to the browser
        const chatroom = JSON.parse(fs.readFileSync("./data/chatroom.json"));
        socket.emit("messages", JSON.stringify(chatroom));
    });

    socket.on("post message", (content) => {
        // Add the message to the chatroom
        if(socket.request.session.user){
            const user = socket.request.session.user;
            const {username, avatar, name} = user;
            const datetime = new Date();
            const message = {user, datetime, content};

            const chatroom = JSON.parse(fs.readFileSync("./data/chatroom.json"));
            chatroom.push(message);
            fs.writeFileSync("./data/chatroom.json", JSON.stringify(chatroom, null, " "));
            io.emit("add message", JSON.stringify(message));
        }
    });

    socket.on("typing", () => {
        const {username, avatar, name} = socket.request.session.user;
        io.emit("get typing", username);
    });
});

// Use a web server to listen at port 8000
httpServer.listen(8000, () => {
    console.log("The chat server has started...");
});
