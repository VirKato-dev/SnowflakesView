###
Анимация снежинок в одном View

#
<!---Пример кода-->
[![Typing SVG](https://readme-typing-svg.herokuapp.com?color=%2336BCF7&lines=Один+из+вариантов)](https://git.io/typing-svg)

#
SnowflakesView sf = new SnowflakesView(this);

или

#
SnowflakesView sf = findViewById(R.id. ...);

sf.setColor(цвет).setCount(количество);
// цвет - int
// параметры можно менять в любой момент

[Главный код виджета](https://github.com/VirKato-dev/SnowflakesView/blob/master/app/src/main/java/my/example/snowflakesview/view/SnowflakesView.java)
