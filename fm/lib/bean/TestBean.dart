class TestBean {
  Data data;
  String person;

  TestBean({this.data, this.person});

  TestBean.fromJson(Map<String, dynamic> json) {
    data = json['data'] != null ? new Data.fromJson(json['data']) : null;
    person = json['person'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.data != null) {
      data['data'] = this.data.toJson();
    }
    data['person'] = this.person;
    return data;
  }
}

class Data {
  String age;
  String name;

  Data({this.age, this.name});

  Data.fromJson(Map<String, dynamic> json) {
    age = json['age'];
    name = json['name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['age'] = this.age;
    data['name'] = this.name;
    return data;
  }
}
