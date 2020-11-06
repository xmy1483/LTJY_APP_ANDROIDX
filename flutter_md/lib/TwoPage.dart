import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';

class TwoPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'PageTwo',
      home:  new MaterialApp(
        home: new Scaffold(
          backgroundColor: Color.fromRGBO(232, 232, 232, 1),
          body: HomeBody(),
        ),
      ),
    );
  }
}


class HomeBody extends StatefulWidget {

  @override
  State createState() {
    return new HomeBodyState();
  }
}

class HomeBodyState extends State<HomeBody> {

  Future<String> callAndroid() async {
    const platform = const MethodChannel("globalChannel");
    String result = await platform.invokeMethod('getBatteryLevel');
  }

  @override
  Widget build(BuildContext context) {
    return RaisedButton(
      child: Container(
        margin: EdgeInsets.symmetric(vertical: 100,horizontal: 40),
        child: Text("PageTwo"),
      ),
      onPressed: ()=> callAndroid(),
    );
  }
}



























