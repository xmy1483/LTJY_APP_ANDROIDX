import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class UserCenterPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        backgroundColor: Color.fromRGBO(232, 232, 232, 1),
        body: BodyView(),
      ),
    );
  }
}

class BodyView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        BlueHeader(),
        OptionsList(),
      ],
    );
  }
}

class OptionItem extends StatelessWidget {
  OptionItem({this.icon,this.name,this.clickListener});
  IconData icon;
  String name;
  Function clickListener;


  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 10,horizontal: 10),
        color: Colors.white,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(icon,color: Colors.blue,),
                Padding(padding: EdgeInsets.only(left: 10),),
                Text(name,style: TextStyle(fontSize: 18),),
              ],
            ),
            Icon(Icons.arrow_right,color: Colors.green,)
          ],
        ),
      ),
    );
  }
}

class OptionsList extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Divider(height: 10,),
        OptionItem(icon: Icons.favorite,name: "账单",),
        Divider(height: 10,),
        OptionItem(icon: Icons.favorite,name: "我的地址",),
        OptionItem(icon: Icons.favorite,name: "我的加油卡",),
        Divider(height: 10,),
        OptionItem(icon: Icons.favorite,name: "副卡余额",),
        OptionItem(icon: Icons.favorite,name: "副卡使用记录",),
        Divider(height: 10,),
        OptionItem(icon: Icons.favorite,name: "设置",),
        OptionItem(icon: Icons.favorite,name: "联系客服",),
        OptionItem(icon: Icons.favorite,name: "常见问题",),
        OptionItem(icon: Icons.favorite,name: "关于我们",),
      ],
    );
  }
}


class BlueHeader extends StatefulWidget {
  @override
  State createState() {
    return HeaderState();
  }
}

class InfoItem extends StatelessWidget {
  InfoItem({this.name,this.value});
  String name;
  String value;
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(value,style: TextStyle(color: Colors.white,fontSize: 18,fontWeight: FontWeight.bold),),
        Text(name,style: TextStyle(color:Colors.white,fontSize: 15),),
      ],
    );
  }
}

class HeaderState extends State<BlueHeader> {
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Colors.red,
            Colors.lightBlue
          ],
        ),
      ),
      margin: EdgeInsets.only(top: 30),
      padding: EdgeInsets.only(left: 10,right: 10,top: 20,bottom: 10),
//      color: Colors.lightGreen,
      width: MediaQuery.of(context).size.width,
      child: Column(
        children: [
          Row(
            children: [
              Padding(padding: EdgeInsets.only(left: 10),),
              Image.asset("image/ic_user.png",fit: BoxFit.contain,width: 50,),
              Padding(padding: EdgeInsets.only(left: 10),),
              Text("19905501230",style: TextStyle(color: Colors.white,fontSize: 20),),
            ],
          ),
          Divider(height: 6,color:Colors.transparent,),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              InfoItem(name:"揩油宝",value:"12.32"),
              InfoItem(name:"优惠券",value:"12.32"),
              InfoItem(name:"消息",value:"12.32"),
            ],
          )
        ],
      ),
    );
  }
}
