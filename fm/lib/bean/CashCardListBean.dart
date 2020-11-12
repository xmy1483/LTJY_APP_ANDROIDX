class CashCardListBean {
  List<CashCardList> cashCardList;
  int errorId;
  List<FuChongCardList> fuChongCardList;

  CashCardListBean({this.cashCardList, this.errorId, this.fuChongCardList});

  CashCardListBean.fromJson(Map<String, dynamic> json) {
    if (json['cashCardList'] != null) {
      cashCardList = new List<CashCardList>();
      json['cashCardList'].forEach((v) {
        cashCardList.add(new CashCardList.fromJson(v));
      });
    }
    errorId = json['errorId'];
    if (json['fuChongCardList'] != null) {
      fuChongCardList = new List<FuChongCardList>();
      json['fuChongCardList'].forEach((v) {
        fuChongCardList.add(new FuChongCardList.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.cashCardList != null) {
      data['cashCardList'] = this.cashCardList.map((v) => v.toJson()).toList();
    }
    data['errorId'] = this.errorId;
    if (this.fuChongCardList != null) {
      data['fuChongCardList'] =
          this.fuChongCardList.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class CashCardList {
  String cardImg;
  String cardName;
  String originPrice;
  String salePrice;

  CashCardList({this.cardImg, this.cardName, this.originPrice, this.salePrice});

  CashCardList.fromJson(Map<String, dynamic> json) {
    cardImg = json['cardImg'];
    cardName = json['cardName'];
    originPrice = json['originPrice'];
    salePrice = json['salePrice'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['cardImg'] = this.cardImg;
    data['cardName'] = this.cardName;
    data['originPrice'] = this.originPrice;
    data['salePrice'] = this.salePrice;
    return data;
  }
}

class FuChongCardList {
  String cardImg;
  String cardName;
  String originPrice;
  String salePrice;

  FuChongCardList({this.cardImg, this.cardName, this.originPrice, this.salePrice});

  FuChongCardList.fromJson(Map<String, dynamic> json) {
    cardImg = json['cardImg'];
    cardName = json['cardName'];
    originPrice = json['originPrice'];
    salePrice = json['salePrice'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['cardImg'] = this.cardImg;
    data['cardName'] = this.cardName;
    data['originPrice'] = this.originPrice;
    data['salePrice'] = this.salePrice;
    return data;
  }
}
