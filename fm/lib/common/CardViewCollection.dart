import 'package:flutter/material.dart';

class CardView extends StatelessWidget {
  CardView({this.child,this.bgColor,this.padding});
  Widget child;
  double padding = 10;
  Color bgColor = Colors.white;
  @override
  Widget build(BuildContext context) {
    return new Container(
      margin: EdgeInsets.symmetric(vertical: 2,horizontal: 10),
      child: new ClipRRect(
        child: Container(
          padding: EdgeInsets.all(padding),
          color: Colors.white,
          child: child,
        ),
        borderRadius: BorderRadius.circular(10),
      ),
    );
  }
}
