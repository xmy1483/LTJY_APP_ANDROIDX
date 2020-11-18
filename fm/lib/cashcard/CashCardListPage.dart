import 'dart:async';
import 'dart:convert';
import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fm/bean/CashCardListBean.dart';

import '../common/ActionBar.dart';
import 'CashCardDetailPage.dart';

// 界面根view
class CashCardListPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Color.fromRGBO(232, 232, 232, 1),
        body: BodyView(),
      );
  }
}

// body部分
class BodyView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Column(
      children: [
        ActionBar(title: "定额卡"), // 标题栏
        Expanded(child: CashCardListView()), // 卡列表
      ],
    );
  }
}

List<Widget> cardListData = [];

// listView
class CashCardListView extends StatelessWidget {
  Future<CashCardListBean> getCardListData() async {
    await Future.delayed(Duration(seconds: 2),(){});
    Completer<CashCardListBean> cmp = new Completer<CashCardListBean>();
    String method = "getCashCardListData";
    const platform = const MethodChannel('globalChannel');
    String result = await platform.invokeMethod(method);
    CashCardListBean dataList = CashCardListBean.fromJson(json.decode(result));
    cmp.complete(dataList);
    return cmp.future;
  }

  @override
  Widget build(BuildContext context) {

    return FutureBuilder<CashCardListBean>(
      future: getCardListData(),
      builder: (BuildContext context, AsyncSnapshot<CashCardListBean> snap) {
        if(snap.connectionState == ConnectionState.done) {
          if(snap.hasError) {
            print("= == === 请求失败");
            return Text("请求失败");
          } else {
            print("= == === 请求成功");
            initCashCardBeanData(snap.requireData);
            return new ListView(children: cardListData,);
          }
        } else {
          print("= == === 请求正在进行中");
          return Text("请求正在进行中");
        }
      },
    );
  }

  initCashCardBeanData(CashCardListBean bean) {
    cardListData.add(Align(alignment: Alignment.centerLeft, child: Text("   定额卡",textAlign: TextAlign.start,style: TextStyle(fontSize: 20, color: Colors.black)),));
    cardListData.add(Padding(padding: EdgeInsets.symmetric(vertical: 3, horizontal: 0)),);

    for (int i = 0; i < bean.cashCardList.length; i++) {
      var item = bean.cashCardList[i];
      cardListData.add(   CardItemView(price: item.salePrice, originPrice: item.originPrice, cardName: item.cardName, imgPath: "image/c.png"),);
    }

    cardListData.add(Align(alignment: Alignment.centerLeft, child: Text("   复充卡",textAlign: TextAlign.start,style: TextStyle(fontSize: 20, color: Colors.black)),));
    cardListData.add(Padding(padding: EdgeInsets.symmetric(vertical: 3, horizontal: 0)),);

    for (int i = 0; i < bean.fuChongCardList.length; i++) {
      var item = bean.fuChongCardList[i];
      cardListData.add(   CardItemView(price: item.salePrice, originPrice: item.originPrice, cardName: item.cardName, imgPath: "image/c.png"),);
    }
    cardListData.add(   Padding(padding: EdgeInsets.only(bottom: 15),),);
  }
}

// CardItemView
// ignore: must_be_immutable
class CardItemView extends StatelessWidget {
  CardItemView({this.price, this.originPrice, this.cardName, this.imgPath});

  String price = "0";
  String cardName = "中国石化加油卡";
  String originPrice = "0";
  String imgPath = "image/a.png";
  BuildContext context;

  @override
  Widget build(BuildContext context) {
    this.context = context;
    Widget container = Container(
      color: Colors.white,
      padding: EdgeInsets.all(10),
      child: Row(
        children: [
          Padding(
            padding: EdgeInsets.only(left: 10),
          ),
          Expanded(
            flex: 1,
            child: Image.asset(
              imgPath,
              fit: BoxFit.cover,
            ),
          ),
          Expanded(
              flex: 2,
              child: Container(
                margin: EdgeInsets.symmetric(vertical: 0, horizontal: 10),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      cardName,
                      style:
                      TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    Text(
                      "现货包邮，支持持中国石化1000元加油卡，中国石化是什么没听说过",
                      overflow: TextOverflow.ellipsis,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Row(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            Text(
                              "￥" + price + "元",
                              style: TextStyle(
                                  fontSize: 17,
                                  color: Colors.red,
                                  fontWeight: FontWeight.bold),
                              overflow: TextOverflow.fade,
                            ),
//                            Padding(
//                                padding: EdgeInsets.symmetric(
//                                    vertical: 0, horizontal: 3)),
                 /*           Text(
                              "原价￥" + originPrice + "元",
                              style: TextStyle(
                                  decoration: TextDecoration.lineThrough),
                              textAlign: TextAlign.center,
                              overflow: TextOverflow.fade,
                            ),*/
                          ],
                        ),
                        Text("立即购买>",overflow: TextOverflow.fade,),
                      ],
                    ),
                  ],
                ),
              ))
        ],
      ),
    );

    return GestureDetector(
      child: new Container(
        margin: EdgeInsets.symmetric(vertical: 2, horizontal: 10),
        child: new ClipRRect(
          child: container,
          borderRadius: BorderRadius.circular(10),
        ),
      ),
      onTap: _onItemClick,
    );
  }

  _onItemClick() async{
    print("点击了：" + price);
    const platform = const MethodChannel('globalChannel');

    var pm = <String,String>{"msg":"msgStr"};
    String result = await platform.invokeMethod("showToast",pm);
    Navigator.push(context, MaterialPageRoute(builder: (_) {
      return new CashCardDetailPage(
        paramPrice: double.parse(price),
      );
    }));
  }
}
