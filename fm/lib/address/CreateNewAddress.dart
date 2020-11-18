import 'dart:convert';

import 'package:city_pickers/city_pickers.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fm/bean/TestBean.dart';
import 'package:fm/common/ActionBar.dart';

class CreateNewAddressPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.white,
        body: BodyView(),
      );
  }
}

TextEditingController etName = new TextEditingController();
TextEditingController etPhone = new TextEditingController();
TextEditingController etPCA = new TextEditingController(); // 省市区
TextEditingController etDetailAddr = new TextEditingController();

var mContext;

class BodyView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    mContext = context;
    return SingleChildScrollView(
      child: Column(
        children: [
          ActionBar(title: '编辑地址'),
          Divider(
              height: 10,
              thickness: 10,
              color: Color.fromARGB(1, 222, 222, 222)),
          EditView(hint: '请输入姓名', controller: etName),
          EditView(hint: '请输手机号', controller: etPhone),
          Container(
            color: Colors.white,
            padding: EdgeInsets.only(right: 10),
            child: Row(
              children: [
                Expanded(
                  flex: 1,
                  child: EditView(
                    hint: '请选择省市区',
                    controller: etPCA,
                  ),
                ),
                GestureDetector(
                  child: Icon(
                    Icons.add_location,
                    color: Colors.blueAccent,
                  ),
                  onTap: selectPCA,
                )
              ],
            ),
          ),
          EditView(
            hint: '请输入详细地址',
            controller: etDetailAddr,
          ),
          Container(
            padding: EdgeInsets.symmetric(horizontal: 10),
            margin: EdgeInsets.only(top: 40),
            child: MaterialButton(
              onPressed: onSubmitClick,
              color: Colors.blueAccent,
              child: Text(
                '提交',
                style: TextStyle(
                  color: Colors.white,
                ),
              ),
              minWidth: MediaQuery.of(context).size.width,
              padding: EdgeInsets.symmetric(vertical: 13),
            ),
          )
        ],
      ),
    );
  }

  Result resultArr = new Result();

  void selectPCA() async {
    Result result = await CityPickers.showCityPicker(
        context: mContext,
        locationCode: resultArr != null
            ? resultArr.areaId ?? resultArr.cityId ?? resultArr.provinceId
            : null,
        cancelWidget: Text('取消'),
        confirmWidget: Text(
          '确定',
          style: TextStyle(color: Colors.blueAccent),
        ),
        height: 220);
    resultArr = result;
    etPCA.text = resultArr.provinceName +
        ' ' +
        resultArr.cityName +
        ' ' +
        resultArr.areaName;
  }

  void onSubmitClick() {
    callAndroid();
  }

  Future<String> callAndroid() async {
    const platform = const MethodChannel('globalChannel');
    String result = await platform.invokeMethod('getBatteryLevel');
    print("原始： " + result);
    TestBean b = TestBean.fromJson(json.decode(result));
    for (int i = 0; i < 10; i++) {
      print("index: $i ");
    }
    print(b.person);
    print(b.data.name);
    print(b.data.age);
//    String result = "{'data':{'age':'23','name':'xmy'},'person':'coder'}";
//    Map<String,dynamic> user = json.decode(result);
//    String person = user["person"];
//    etName.text = person;
//    print(person);
  }
}

class EditView extends StatelessWidget {
  EditView({this.hint, this.controller});

  TextEditingController controller;
  String hint = '';

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      padding: EdgeInsets.symmetric(horizontal: 10),
      child: TextField(
        decoration: InputDecoration(
          hintText: hint,
        ),
        controller: controller,
      ),
    );
  }
}
