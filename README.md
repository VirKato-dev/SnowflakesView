###
Анимация снежинок в одном View

#
SnowflakesView sf = new SnowflakesView(this);
или
SnowflakesView sf = findViewById(R.id. ...);

sf.setColor(цвет).setCount(количество);
// цвет - int
// параметры можно менять в любой момент