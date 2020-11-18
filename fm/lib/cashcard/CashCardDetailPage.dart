import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fm/common/ActionBar.dart';
import 'package:fm/common/CardViewCollection.dart';

void main() => runApp(new CashCardDetailPage());

BottomPayView bottomPayView;
double cardPrice = 1000; // 当前卡的面额
   
// 界面根view
class CashCardDetailPage extends StatelessWidget {
  double paramPrice;
  CashCardDetailPage({this.paramPrice});
  @override
  Widget build(BuildContext context) {
    cardPrice = this.paramPrice;
    print(cardPrice.toString());
    return Scaffold(
        backgroundColor: Color.fromRGBO(232, 232, 232, 1),
        body: BodyView(),
      );
  }
}

class BodyView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    bottomPayView = new BottomPayView();
    return Stack(
      alignment: AlignmentDirectional.topStart,
      children: [
        new Column(
          children: [
            ActionBar(title: "确认订单",),
            LocationView(),
            Divider(height: 10,color: Colors.transparent,),
            CardIntroduceView(),
            Divider(height: 10,color: Colors.transparent,),
            PayGroupsView(),
          ],
        ),
        Positioned(
          bottom: 0,
          left: 0,
          right: 0,
          child: bottomPayView,
        )
      ],
    );
  }
}

// 底部支付按钮和统计
class BottomPayView extends StatefulWidget {
  final BottomPayState mState = new BottomPayState();
  updateMoney(newMoney) {
    mState.setMoney(newMoney);
  }
  updateCount(newCount) {
    mState.setCount(newCount);
  }
  @override
  State createState() {
    return mState;
  }
}

class BottomPayState extends State<BottomPayView> {
  String money = cardPrice.toString();
  String count = "1";

  setMoney(newMoney) {
    setState(() {
      this.money = newMoney;
    });
  }

  setCount(newCount) {
    setState(() {
      this.count = newCount;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      child:  Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Padding(padding: EdgeInsets.all(0)),
          Wrap(
            crossAxisAlignment: WrapCrossAlignment.center,
            children: [
              Text("总计 "+ count +" 件，共 "),
              Text(money+"元",style: TextStyle(fontWeight: FontWeight.bold,color: Colors.red,fontSize: 18),),
            ],
          ),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 5,horizontal: 10),
            child: RaisedButton(
              child: Text("支付"),
              padding: EdgeInsets.symmetric(vertical: 0,horizontal: 30),
              color: Colors.lightBlue,
              textColor: Colors.white,
              onPressed: _submitOrder,
            ),
          )
        ],
      ),
    );
  }

  _submitOrder() async{
    print("点击支付：");



  }
}

class PayGroupsView extends StatefulWidget {
  @override
  State createState() {
    return PayGroupState();
  }
}

class PayGroupState extends State<PayGroupsView> {
  @override
  Widget build(BuildContext context) {
    return CardView(
      padding: 10,
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Wrap(
                children: [
                  Image.asset("image/ic_alipay.png",fit: BoxFit.contain,width: 30,),
                  Padding(padding: EdgeInsets.only(left: 10),child: Text("支付宝"),)
                ],
              ),
              Icon(Icons.check_box,color: Colors.green,)
            ],
          ),
          Divider(height: 10,color: Colors.transparent),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Wrap(
                children: [
                  Image.asset("image/ic_wechat_pay.png",fit: BoxFit.contain,width: 30,),
                  Padding(padding: EdgeInsets.only(left: 10),child: Text("微信支付"),)
                ],
              ),
              Icon(Icons.check_box,color: Colors.green,)
            ],
          ),
          Divider(height: 10,color: Colors.transparent),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Wrap(
                children: [
                  Image.asset("image/ic_alipay.png",fit: BoxFit.contain,width: 30,),
                  Padding(padding: EdgeInsets.only(left: 10),child: Text("云闪付支付"),)
                ],
              ),
              Icon(Icons.check_box,color: Colors.green,)
            ],
          ),
        ],
      ),
    );
  }
}



class CardIntroduceView extends StatefulWidget {
  @override
  State createState() {
    return new CardIntroduceState();
  }
}

class CardIntroduceState extends State<CardIntroduceView> {

  String price = "1234";
  String originPrice = "222";
  String cardName = cardPrice.toString()+"元中国石化加油卡";
  String imgPath = "image/b.png";

  int count = 1;

  _countAdd() {
    setState(() {
      count ++;
      _updateBottomView();
    });
  }
  // 重新计算支付信息
  _updateBottomView() {
    bottomPayView.updateCount(count.toString());
    bottomPayView.updateMoney((count*cardPrice).toString());
  }

  _countSubtract(){
    if(count>1) {
      setState(() {
        count --;
        _updateBottomView();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return CardView(
      padding: 10,
      child: Column(
        children: [
          Row(
            children: [
              Padding(padding: EdgeInsets.only(left: 10),),
              Expanded(
                flex: 1,
                child: Image.asset(imgPath,fit: BoxFit.cover,),
              ),
              Expanded(
                  flex: 2,
                  child: Container(
                    margin: EdgeInsets.symmetric(vertical: 0,horizontal: 10),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(cardName,style: TextStyle(fontSize: 18,fontWeight: FontWeight.bold),),
                        Text("现货包邮，支持持中国石化1000元加油卡，中国石化是什么没听说过",overflow: TextOverflow.fade,),
                        // 卡介绍部分
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Wrap(
                              crossAxisAlignment: WrapCrossAlignment.center,
                              children: [
                                Text("￥"+price+"元", style: TextStyle(fontSize: 17,color: Colors.red,fontWeight: FontWeight.bold),),
                                Padding(padding: EdgeInsets.symmetric(vertical: 0,horizontal: 3)),
                                Text("原价￥"+originPrice+"元", style: TextStyle(decoration: TextDecoration.lineThrough),textAlign: TextAlign.center,),
                              ],
                            ),
                          ],
                        ),
                      ],
                    ),
                  )
              )
            ],
          ),
          // 数量加减
          Divider(height: 10,color: Colors.transparent,),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text("购买数量"),
              Wrap(
                children: [
                  GestureDetector(
                    child: Container(
                      color: Colors.blue,
                      height: 24,
                      width: 24,
                      alignment: Alignment.center,
                      child: Text("-",style: TextStyle(color:Colors.white),),
                    ),onTap: _countSubtract,
                  ),

                  Container(
                    height: 24,
                    width: 33,
                    alignment: Alignment.center,
                    child: Text(count.toString(),),
                  ),

                  GestureDetector(
                    child: Container(
                      color: Colors.blue,
                      height: 24,
                      width: 24,
                      alignment: Alignment.center,
                      child: Text("+",style: TextStyle(color:Colors.white),),
                    ),onTap: _countAdd,
                  ),
                ],
              )
            ],
          ),
          Divider(height: 10,color: Colors.transparent,),
          // 说明
          Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text("商品说明",),
              Expanded(
                  flex: 1,
                  child: Container(
                    padding: EdgeInsets.only(left: 30),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text("1.全国中石化加油站通用，可加汽油，不可加乙醇汽油和柴油;",overflow: TextOverflow.fade,),
                        Text("2.不记名不挂失不可兑换不可续充;",overflow: TextOverflow.fade,),
                        Text("3.自签收之日起有效期一年,过期作废;",overflow: TextOverflow.fade,),
                        Text("4.购买/消费此卡不提供任何发票.;",overflow: TextOverflow.fade,)
                      ],
                    ),
                  )
              )
            ],
          )
        ],
      ),
    );
  }
}


class LocationView extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: CardView(
        padding: 10,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Expanded(
              child: Image.asset("image/ic_location.png",fit: BoxFit.contain,height: 50,width: 30,),
            ),
            Expanded(
              flex: 4,
              child:Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Wrap(
                    crossAxisAlignment: WrapCrossAlignment.center,
                    children: [
                      Text("理想够", style: TextStyle(fontWeight: FontWeight.bold),),
                      Padding(padding: EdgeInsets.only(left: 10,),child: Text("19905502345",),)
                    ],
                  ),
                  Text("化身大到12号五楼两个电梯或者三个电梯这边都可以也不可以",overflow: TextOverflow.fade,)
                ],
              ) ,
            ),
            Expanded(
              flex: 1,
              child: Padding(
                padding: EdgeInsets.only(right: 10),
                child: Text(">",textAlign: TextAlign.end,),
              ),
            )
          ],
        ),
      ),
    );
  }
}
