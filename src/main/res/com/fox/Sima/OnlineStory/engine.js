// ======================================
// Core
// ======================================
var Sima = {};

// Sima Vars
Sima.delay = 300;
Sima.version = "1.1";
Sima.author = "<font color=#add8e6>Haxi Denti Ihor-fox @AldieStarProject</font>";
Sima.product = "<font color=red>Sima Online Story</font>";


// =========================================
// =========================================
// =========================================
Sima.get = function (id) {
    return document.getElementById(id);
};

Sima.waterMark = function(){
    var el = document.createElement("p");
    document.body.appendChild(el);
    el.style.position = "fixed";
    el.style.left = "10px";
    el.style.bottom = "10px";
    el.innerHTML = Sima.product + " ver: " + Sima.version + " made by:<br>"+Sima.author+"<br>"+
        "<a href='/f/edit.html' class='editButton'>EDIT</a>";
    el.style.color = "white";
    el.style['font-size'] = '10px';
};
Sima.waterMark();


// ======================================
// For Changing BackGround
// ======================================

Sima.bg = function (img) {
    Sima.get("background").style['background-image'] = "url("+img+")";
};

Sima.db = function (key, val) {
    if (val == undefined){
        // Get
        return Sima.ajax("/db/get/"+key);
    }
    // Set
    return Sima.ajax("/db/set/"+key+"/"+btoa(val));
};


// ======================================
// Width && Height
// ======================================
Sima.wh = function (obj) {
    return {
        w: obj.innerWidth,
        h: obj.innerHeight
    };
};



// ======================================
// Text
// ======================================
Sima.setText = function (txt) {
    if (txt == undefined) {
        // Get
        return Sima.get("text").innerHTML;
    }

    // Raplacing
    txt = txt.replace(/\n/g, "<br>");

    // Set
    Sima.get("text").innerHTML = "<p>"+txt+"</p>";
};




// ======================================
// Buttons
// ======================================
Sima.button = function(name, onclick){
    var el = document.createElement("button");
    var where = Sima.get("buttons");
    where.appendChild(el);

    // Checking 'onclick'
    // If it function
    if (onclick instanceof Function) {
        el.onclick = onclick;
    } else {
        // If 'onclick' is a String
        el.onclick = function () {
            eval(onclick);
        }
    }
    el.innerText = name;

    // ==========

};

Sima.delButtons = function(){
    var el = Sima.get("buttons");
    el.innerHTML = "";
};




// ======================================
// Change Scene mode
// ======================================

Sima.text = function(sceneText, msec){
    if (msec == undefined) msec = 500;
    var textElem = Sima.get("text")
    var buttons = Sima.get("buttons");

    // Clear all buttons
    buttons.innerHTML = "";

    // Make invisible
    textElem.style.transition = "300ms";
    textElem.style.opacity = 0;
    // Make it visibe -- Function
    var makeItVisible = function(){
        Sima.setText(sceneText);
        textElem.style.opacity = 1;
    };
    // Run after msec this Function
    setTimeout(makeItVisible, msec);
};



// ======================================
// Scene System
// ======================================

Sima.scenes = {};

Sima.currentScene = "0";

/**
 * Scene
 *  {onload: Load the Script,
 *   buttons: Array of buttons
 *  }
 *
 *  Buttons:
 *   [ // Array
 *     {name: Name of the button,
 *      onclick: onclick()
 *     }
 *   ]
 * @param name
 */
Sima.scene = function (name) {
    // if name is undefined, then loading current Scene
    if (name == undefined) name = Sima.currentScene;

    var sc = Sima.scenes[name];
    if (sc == undefined){
        console.log("Error! No that scene: "+name);
        return;
    }
    // ---
    // Loading
    // ---
    var script = sc.onload;
    // ms to wait, until buttons will appear back
    var wait = sc.waitms;
    if (wait == undefined) wait = 500;
    // Waiting
    Sima.lock(wait);
    // ---
    if (script == undefined){
        console.log("No script in scene: "+name);
        return;
    }
    // ---
    // Run Script of the Scene
    // ---
    if (script instanceof Function){
        // If it is Function
        script();
    } else {
        // Or String :)
        eval(script);
    }

    // If success
    Sima.currentScene = name;
    Sima.endingOfTheScene();
};

Sima.createScene = function (name, onload, waitms) {
    var created = {onload: onload, waitms: waitms};
    Sima.scenes[name] = created;
    return created;
};




// Locking buttons
Sima.lock = function(ms){
    if (ms < 400) ms = 400;
    // Get buttons field
    var el = Sima.get("buttons");
    // ms = 500 if it was undefined
    if (ms == undefined) ms = 500;
    // ---
    el.style.opacity = 0;
    // Make it invisible
    // By using display: none -- after 300 ms
    // ---
    el.style.display = "none";
    // ---
    // Make it visible back
    // ---
    setTimeout(function(){
       el.style.display = "block";
    }, ms/2);
    // ---
    // Ending
    // ---
    setTimeout(function(){
        el.style.opacity = 1;
    }, ms);
};


// Special variables for ENDING
// ----
var _t = "";
var _bs = [];
// ----

Sima.endingOfTheScene = function(){
    // Text output
    Sima.text(_t);
    _t = ""; // Clearing

    // Buttons
    // Foreach in _bs array :)
    for (var i = 0; i < _bs.length; i++){
        Sima.button(_bs[i].name, _bs[i].click);
    }
    _bs = []; // Clearing
};










// ======================================
// Make LIFE easier :)
// Shorting functions
// ======================================


function t(txt){
    _t += "<br>"+txt;
}


function b(name, click){
    _bs.push({
        name: name, click: click
    });
}

function goto(name){
    Sima.scene(name);
}

function bg(src){
    Sima.bg(src);
}

function currentScene(name){
    Sima.currentScene = name;
}



// Locking buttons
// OR: wait (w)
function w(ms){
    Sima.lock(ms);
};

// Online DB
function db(k, v){
    Sima.db(k, v);
}

// New Scene
function scene(name, script){
    Sima.createScene(name, script);
}





// ======================================
// Starting
// ======================================
window.addEventListener("load", function(){
    Sima.scene();
});