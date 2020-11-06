package com.bac.bihupapa.bean;

import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.beans.insurance
 * 创建人：Wjz
 * 创建时间：2017/1/17
 * 类描述：
 */

public class InsuranceAddressBean {


    /**
     * citys : [{"areas":[{"area":"320802","area_name":"清河区"},{"area":"320801","area_name":"市辖区"},{"area":"320831","area_name":"金湖县"},{"area":"320830","area_name":"盱眙县"},{"area":"320829","area_name":"洪泽县"},{"area":"320826","area_name":"涟水县"},{"area":"320811","area_name":"清浦区"},{"area":"320804","area_name":"淮阴区"},{"area":"320803","area_name":"楚州区"}],"city":"320800","city_name":"淮安市"},{"areas":[{"area":"320202","area_name":"崇安区"},{"area":"320201","area_name":"市辖区"},{"area":"320282","area_name":"宜兴市"},{"area":"320281","area_name":"江阴市"},{"area":"320211","area_name":"滨湖区"},{"area":"320206","area_name":"惠山区"},{"area":"320205","area_name":"锡山区"},{"area":"320204","area_name":"北塘区"},{"area":"320203","area_name":"南长区"}],"city":"320200","city_name":"无锡市"},{"areas":[{"area":"321112","area_name":"丹徒区"},{"area":"321111","area_name":"润州区"},{"area":"321102","area_name":"京口区"},{"area":"321101","area_name":"市辖区"},{"area":"321183","area_name":"句容市"},{"area":"321182","area_name":"扬中市"},{"area":"321181","area_name":"丹阳市"}],"city":"321100","city_name":"镇江市"},{"areas":[{"area":"320502","area_name":"沧浪区"},{"area":"320501","area_name":"市辖区"},{"area":"320585","area_name":"太仓市"},{"area":"320584","area_name":"吴江市"},{"area":"320583","area_name":"昆山市"},{"area":"320582","area_name":"张家港市"},{"area":"320581","area_name":"常熟市"},{"area":"320507","area_name":"相城区"},{"area":"320506","area_name":"吴中区"},{"area":"320505","area_name":"虎丘区"},{"area":"320504","area_name":"金阊区"},{"area":"320503","area_name":"平江区"}],"city":"320500","city_name":"苏州市"},{"areas":[{"area":"320724","area_name":"灌南县"},{"area":"320723","area_name":"灌云县"},{"area":"320722","area_name":"东海县"},{"area":"320721","area_name":"赣榆县"},{"area":"320706","area_name":"海州区"},{"area":"320705","area_name":"新浦区"},{"area":"320703","area_name":"连云区"},{"area":"320701","area_name":"市辖区"}],"city":"320700","city_name":"连云港市"},{"areas":[{"area":"320125","area_name":"高淳县"},{"area":"320124","area_name":"溧水县"},{"area":"320116","area_name":"六合区"},{"area":"320115","area_name":"江宁区"},{"area":"320114","area_name":"雨花台区"},{"area":"320113","area_name":"栖霞区"},{"area":"320111","area_name":"浦口区"},{"area":"320107","area_name":"下关区"},{"area":"320106","area_name":"鼓楼区"},{"area":"320105","area_name":"建邺区"},{"area":"320104","area_name":"秦淮区"},{"area":"320103","area_name":"白下区"},{"area":"320102","area_name":"玄武区"},{"area":"320101","area_name":"市辖区"}],"city":"320100","city_name":"南京市"},{"areas":[{"area":"320482","area_name":"金坛市"},{"area":"320481","area_name":"溧阳市"},{"area":"320412","area_name":"武进区"},{"area":"320411","area_name":"新北区"},{"area":"320405","area_name":"戚墅堰区"},{"area":"320404","area_name":"钟楼区"},{"area":"320402","area_name":"天宁区"},{"area":"320401","area_name":"市辖区"}],"city":"320400","city_name":"常州市"},{"areas":[{"area":"321088","area_name":"江都市"},{"area":"321084","area_name":"高邮市"},{"area":"321081","area_name":"仪征市"},{"area":"321023","area_name":"宝应县"},{"area":"321011","area_name":"维扬区"},{"area":"321003","area_name":"邗江区"},{"area":"321002","area_name":"广陵区"},{"area":"321001","area_name":"市辖区"}],"city":"321000","city_name":"扬州市"},{"areas":[{"area":"320684","area_name":"海门市"},{"area":"320683","area_name":"通州市"},{"area":"320682","area_name":"如皋市"},{"area":"320681","area_name":"启东市"},{"area":"320623","area_name":"如东县"},{"area":"320621","area_name":"海安县"},{"area":"320611","area_name":"港闸区"},{"area":"320602","area_name":"崇川区"},{"area":"320601","area_name":"市辖区"}],"city":"320600","city_name":"南通市"},{"areas":[{"area":"320382","area_name":"邳州市"},{"area":"320381","area_name":"新沂市"},{"area":"320324","area_name":"睢宁县"},{"area":"320323","area_name":"铜山县"},{"area":"320322","area_name":"沛县"},{"area":"320321","area_name":"丰县"},{"area":"320311","area_name":"泉山区"},{"area":"320305","area_name":"贾汪区"},{"area":"320304","area_name":"九里区"},{"area":"320303","area_name":"云龙区"},{"area":"320302","area_name":"鼓楼区"},{"area":"320301","area_name":"市辖区"}],"city":"320300","city_name":"徐州市"},{"areas":[{"area":"320982","area_name":"大丰市"},{"area":"320981","area_name":"东台市"},{"area":"320925","area_name":"建湖县"},{"area":"320924","area_name":"射阳县"},{"area":"320923","area_name":"阜宁县"},{"area":"320922","area_name":"滨海县"},{"area":"320921","area_name":"响水县"},{"area":"320903","area_name":"盐都区"},{"area":"320902","area_name":"亭湖区"},{"area":"320901","area_name":"市辖区"}],"city":"320900","city_name":"盐城市"},{"areas":[{"area":"321324","area_name":"泗洪县"},{"area":"321323","area_name":"泗阳县"},{"area":"321322","area_name":"沭阳县"},{"area":"321311","area_name":"宿豫区"},{"area":"321302","area_name":"宿城区"},{"area":"321301","area_name":"市辖区"}],"city":"321300","city_name":"宿迁市"},{"areas":[{"area":"321284","area_name":"姜堰市"},{"area":"321283","area_name":"泰兴市"},{"area":"321282","area_name":"靖江市"},{"area":"321281","area_name":"兴化市"},{"area":"321203","area_name":"高港区"},{"area":"321202","area_name":"海陵区"},{"area":"321201","area_name":"市辖区"}],"city":"321200","city_name":"泰州市"}]
     * province : 320000
     * province_name : 江苏省
     */

    private String province;
    private String province_name;
    private boolean         isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    /**
     * areas : [{"area":"320802","area_name":"清河区"},{"area":"320801","area_name":"市辖区"},{"area":"320831","area_name":"金湖县"},{"area":"320830","area_name":"盱眙县"},{"area":"320829","area_name":"洪泽县"},{"area":"320826","area_name":"涟水县"},{"area":"320811","area_name":"清浦区"},{"area":"320804","area_name":"淮阴区"},{"area":"320803","area_name":"楚州区"}]
     * city : 320800
     * city_name : 淮安市
     */

    private List<CitysBean> citys;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public List<CitysBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CitysBean> citys) {
        this.citys = citys;
    }

    public static class CitysBean {
        private String city;
        private String city_name;

        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        /**
         * area : 320802
         * area_name : 清河区
         */

        private List<AreasBean> areas;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public List<AreasBean> getAreas() {
            return areas;
        }

        public void setAreas(List<AreasBean> areas) {
            this.areas = areas;
        }

        public static class AreasBean {
            private String area;
            private String area_name;

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getArea_name() {
                return area_name;
            }

            public void setArea_name(String area_name) {
                this.area_name = area_name;
            }
        }
    }

    @Override
    public String toString() {
        return "InsuranceAddressBean{" +
                "province='" + province + '\'' +
                ", province_name='" + province_name + '\'' +
                ", isSelect=" + isSelect +
                ", citys=" + citys +
                '}';
    }
}
