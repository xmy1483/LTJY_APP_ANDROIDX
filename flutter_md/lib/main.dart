import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import 'TwoPage.dart';
void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      home:  new MaterialApp(
        home: new Scaffold(
          backgroundColor: Color.fromRGBO(232, 232, 232, 1),
          body: HomeView(),
        ),
      ),
      routes: {
        "pageSec":(context){
          return TwoPage();
        }
      },
    );
  }
}

class TextViewState extends State<TextView> {
  String text = "originText";
  setText(testNew){
    setState(() {
      this.text = testNew;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Text(text,style: TextStyle(fontSize: 10,fontWeight: FontWeight.bold,color: Colors.red),);
  }
}

TextViewState state;
class TextView extends StatefulWidget {
  @override
  State createState() {
    state = new TextViewState();
    return state;
  }
}

class HomeView extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(top:40),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          TextView(),
          RaisedButton(
              child: Text("call native"),
              onPressed: callNativeMethod)
        ],
      ),
    );
  }

  static const platformMethodChannel = const MethodChannel('globalChannel');

  Future meHandler(MethodCall call) async {
    print("...............");
  }

  Future<Null> callNativeMethod() async {
//    platformMethodChannel.setMethodCallHandler((call) => meHandler(call));

    String _message;
    try {
      final String result =
      await platformMethodChannel.invokeMethod('getBatteryLevel');
      _message = result;
      state.setText(_message);
    } on PlatformException catch(e){
      state.setText("exception");
    }
  }

}































