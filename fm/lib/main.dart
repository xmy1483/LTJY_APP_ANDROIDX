import 'package:flutter/material.dart';
import 'package:fm/cashcard/CashCardListPage.dart';
import 'package:fm/user/UserCenter.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: CashCardListPage(),
      routes: <String,WidgetBuilder>{
        "/userCenter": (pms)=> UserCenterPage(),
      },
    );
  }
}
