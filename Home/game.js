// Background image
bg("/res/img01.jpg");

// Set Default Scene on start
currentScene("home");

// Keys
var key = false;

// Scene: home
scene("home", function(){
  t("Вы стоите внутри дома.");
  b("Подойти к столу", "goto('table')");
  b("Подойти к двери", "goto('door')");
}, 500);


// Scene: table
scene("table", function(){
  t("Вы над столом");
  if (key == false){
     t("На столе валяется ключ");
     b("Взять ключ", "key = true; goto()");
  }
  b("Назад", "goto('home')");
});


// Scene: door
scene("door", function(){
    t("Вы возле двери");
    if (key){
        b("Открыть и выйти", "goto('win')");
    }
    b("Назад", "goto('home')");
});



// Scene: win
scene("win", function(){
    t("Вы выиграли!");
    b("Заново!", "goto('home')");
});