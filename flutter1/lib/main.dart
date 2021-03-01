import 'package:flutter/material.dart';

class MyForm extends StatefulWidget{
  @override
  State<StatefulWidget> createState() => MyState();

}

class MyState extends State<MyForm> {
  TextEditingController textEditingController = TextEditingController();
  String displayedText = 'Hello, Flutter';
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Center(child: Column(mainAxisAlignment: MainAxisAlignment.center,
    children: [
      Text(displayedText),
      TextField(controller: textEditingController),
      RaisedButton(child: Text('Change Text!'),
        onPressed: () => setState(() {
            displayedText = textEditingController.text;
          }),
      ),
    ]));
  }

  void _updateWidget() {
    Scaffold.of(context).showSnackBar(
        SnackBar(content: Text("Hi" + textEditingController.text))
    );
    setState(() {});
  }
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      title: 'test',
      home: Scaffold(
        appBar: AppBar(title: Text('title')),
        body: MyForm(),
      )
    );
  }
}

void main() => runApp(MyApp());