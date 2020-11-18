import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fm/common/CardViewCollection.dart';

class CouponPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Color.fromRGBO(232, 232, 232, 1),
        body: BodyView(),
      );
  }
}

class BodyView extends StatefulWidget {
  @override
  State createState() =>BodyViewState();
}

CouponIndicator indicator = new CouponIndicator();
class BodyViewState extends State<BodyView> {
  int i = 0;
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        indicator,
        Expanded(child: CouponPageView(),)
      ],
    );
  }
}

class CouponPageView extends StatefulWidget {
  @override
  State createState() =>CouponPageViewState();
}

PageController pageAdapter = new PageController();
class CouponPageViewState extends State<CouponPageView> {
  @override
  Widget build(BuildContext context) {
    return PageView(
      children: [
        CouponListView(type: 0,),
        CouponListView(type: 1,),
        CouponListView(type: 2,),
      ],
      controller: pageAdapter,
      onPageChanged: (index) {
        indicator.updatePage(index);
      },
    );
  }
}

//=====================================================
class CouponListView extends StatefulWidget {
  CouponListView({this.type});
  int type = 0;
  @override
  State createState() {
    return CouponListState(type: type);
  }
}

class CouponListState extends State<CouponListView> {
  CouponListState({this.type});
  int type = 1;
//  CouponListState({this.type});
  @override
  Widget build(BuildContext context) {
    return ListView(
      children: [
        CouponItem(type: type,),
        CouponItem(type: type,),
        CouponItem(type: type,),
      ],
    );
  }
}
//=====================================================


//============ coupon iren============================
class CouponItem extends StatelessWidget {
  CouponItem({this.type});
  int type = 0;
  String deadLine = "123";

  @override
  Widget build(BuildContext context) {
    List<Widget> chds = [
      Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset("image/ic_user.png",fit: BoxFit.contain,width: 20,),
              Padding(padding: EdgeInsets.only(left: 5),),
              Text("由骆驼加油提供",style: TextStyle(fontWeight: FontWeight.bold,fontSize: 16),),
            ],
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text("￥20.0",style: TextStyle(fontWeight: FontWeight.bold,fontSize: 20),),
              Padding(padding: EdgeInsets.only(left: 5),),
              Text("加油卡充值现金券",style: TextStyle(fontWeight: FontWeight.bold,fontSize: 16),),
            ],
          ),
          Text("有效期至："+deadLine,style: TextStyle(fontWeight: FontWeight.bold,fontSize: 16,color: Colors.grey),),
        ],),
    ];

    if(type == 0) {
      chds.add(     RaisedButton(
        onPressed: (){},
        child: Text("去使用",style: TextStyle(color: Colors.white),),
        color: Colors.blue,
      ));
    } else if(type == 1) {
      chds.add(Text("已使用",style: TextStyle(color: Colors.grey,fontSize: 18),),);
    } else if(type == 2) {
      chds.add(Text("已过期",style: TextStyle(color: Colors.grey,fontSize: 18),),);
    }




    return CardView(bgColor: Colors.white,padding: 10,
    child: Row(
    mainAxisAlignment: MainAxisAlignment.spaceBetween,
    children: chds,
    ));
  }
}


//============ coupon iren============================

//=================== page indicator ==========================================================
class CouponIndicator extends StatefulWidget {
  CouponState state =  CouponState();
  updatePage(int index) {
    state.updatePage(index);
  }

  @override
  State createState() => state;
}

class CouponState extends State<CouponIndicator> {
  Color c0 = Colors.lightBlue;
  Color c1 = Colors.grey;
  Color c2 = Colors.grey;

  updatePage(int index) {
    if(index == 0) {
      c0 = Colors.lightBlue;
      c1 = Colors.grey;
      c2 = Colors.grey;
    } else if(index == 1) {
      c0 = Colors.grey;
      c1 = Colors.lightBlue;
      c2 = Colors.grey;
    } else if(index == 2) {
      c0 = Colors.grey;
      c1 = Colors.grey;
      c2 = Colors.lightBlue;
    }
    this.setState(() {

    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(top: 40),
      padding: EdgeInsets.symmetric(vertical: 10),
      color: Colors.white,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          GestureDetector(
            child: Text("未使用",style: TextStyle(color: c0,fontSize: 20),),
            onTap: (){
              pageAdapter.jumpToPage(0);
            },
          ),GestureDetector(
            child: Text("已使用",style: TextStyle(color: c1,fontSize: 20),),
            onTap: (){
              pageAdapter.jumpToPage(1);
            },
          ),GestureDetector(
            child: Text("已过期",style: TextStyle(color: c2,fontSize: 20),),
            onTap: (){
              pageAdapter.jumpToPage(2);
            },
          ),
        ],
      ),
    );
  }
}
//=================== page indicator ==========================================================




