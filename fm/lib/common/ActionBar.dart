import 'package:flutter/material.dart';

class ActionBar extends StatelessWidget {
  ActionBar({this.title});
  final String title;
  BuildContext context;
  final TextStyle ts = new TextStyle(
    color: Colors.grey,
    backgroundColor: Colors.white,
    decoration: TextDecoration.none,
    fontSize: 20,);

  @override
  Widget build(BuildContext context) {
    this.context = context;
    return new Container(
      margin: EdgeInsets.only(top:24,bottom: 0),
      color: Colors.white,
      padding: EdgeInsets.symmetric(vertical: 8,horizontal: 10,),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          GestureDetector(
            child: Icon(Icons.arrow_back,color: Color.fromRGBO(122, 122, 122, 1),size: 30,),
            onTap: _onBackPressed,
          ),
          Text(title,style: TextStyle(fontSize: 18),),
          Text('')
        ],
      ),
    );
  }

  _onBackPressed() {
    Navigator.of(context).pop(context);
  }
}