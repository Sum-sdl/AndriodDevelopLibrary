package com.sum.andrioddeveloplibrary.protobuffer;

import android.annotation.SuppressLint;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品
 */
public class Goods implements Serializable, Cloneable {

    public static final int OPEN_ALL_PRODUCT_DATE = 1;

    //UI展示数据
    //商品生产日期类型展示 0:具体生产日期 1:从旧到新 2:从新到旧
    public int productionDateSource = 0;

    //是否要显示寻源，默认商品都需要寻源，true:显示寻源,false:一直都不显示寻源
    public boolean isNeedShowDateSource = true;

    public boolean isNeedCheckGoodsDate() {
        return productionDateSource == 0;
    }

    //订货品批次ID
    private Long preOrderBillId;

    //订货品批次名称
    private String preOrderBillNo;

    private Long preOrderBillDetailId;

    private Long id;//商品id

    private Long cid;//经销商id

    private String name;

    private String shortName;

    // 商品档案是否支持改价，true标识支持改价，false标识不支持改价
    private Boolean discount;

    private Byte state;

    private String baseUnitId;///---

    private String baseUnitName;

    private String baseBarcode;

    private String pkgUnitId;//---

    private String pkgUnitName;

    private String pkgBarcode;

    // 大包和小包的换算比例
    private Double unitFactor;

    private String unitFactorName;

    private Double baseWholesale;//单价

    private Double baseRetail;

    private Double baseCheapest;//最低

    private Double basePurchase;

    private Double baseSpecials1;

    private Double baseSpecials2;

    private Double baseSpecials3;

    private Double pkgWholesale;//单价

    private Double pkgRetail;

    private Double pkgCheapest;

    private Double pkgPurchase;

    private Double pkgSpecials1;

    private Double pkgSpecials2;

    private Double pkgSpecials3;

    private Double costPrice;

    private String articleNumber;

    private String specifications;//商品规格
    private Integer shelfLife;//保质期

    private Integer seq;

    private String origPlace;

    private Long supplierId;

    private String otherBarcode;

    private String otherBarcode1;

    private String otherBarcode2;

    private String statisticsId;

    private Integer warnDays;


    private String picture;

    private String picture1;
    private String picture2;
    private String picture3;

    private String queryText;

    private String specifications1;

    /**
     * parentState：记录商品的类型(普通,口味) 0:普通商品 1:口味的父商品 2:口味的子商品
     * <p>
     * calStockByTaste：只用于标识口味父商品有没有开启'分口味核算库存'，仅在parentState=1有意义
     * <p>
     * 2字段组合后的意义
     * calStockByTaste=1 & parentState=1 分口味核算库存商品,标识分口味的主商品
     * calStockByTaste=0 & parentState=1 不分口味核算库存商品,标识不分口味的主商品
     * <p>
     * calStockByTaste在不分口味核算库存商品中,父商品calStockByTaste=0，子商品calStockByTaste=1
     * calStockByTaste在分口味核算库存商品中,父商品calStockByTaste=1，子商品calStockByTaste=0
     * calStockByTaste在普通商品中,calStockByTaste=0
     */
    private Integer parentState;

    private Long parentId;

    private Integer productionDateState;

    //mid unit
    private String midUnitId;

    // 中包和小包的换算比例
    private Double midUnitFactor;
    private String midUnitName;
    private String midBarcode;
    private Double midWholesale;//单价
    private Double midRetail;
    private Double midCheapest;
    private Double midPurchase;
    private Double midSpecials1;
    private Double midSpecials2;
    private Double midSpecials3;

    private Integer calStockByTaste;//分口味核算库存：父商品是1 ； 子商品是0 ； 不分口味核算库存：父商品0 ； 子商品1
    private String tasteBarcode;

    //是否关联单据 0-未关联 1-关联
    private Integer hasBillRecord = 0;

    private List<Goods> subList = new ArrayList<>();

    //-----------------20180301新增------------------
    private Integer typeSelect;//是否选中 ,0未和1选中
    private Integer typeInput;//是否已经填写当前商品的详细信息0未和1选中
    private Integer originaTypeInput;//是否已经填写当前商品的详细信息0未和1选中
    /**
     * 1."销/退"开关受公司设置中“允许在销售订单和销售单中开退货商品”开关的控制，当不允许时，则“销/售”开关隐藏。
     * 2、订货商品不显示退货
     * 3、云仓且销售订单不显示退货
     */
    private boolean isBack;//默认显示销
    private boolean isShowGive;//业务员角色不允许开赠品应该只限制销售，所以销售中的退货应该可以开
    private boolean isShowSaleReject;//1."销/退"开关受公司设置中“允许在销售订单和销售单中开退货商品”开关的控制，当不允许时，则“销/售”开关隐藏。2.退货单和退货订单页面，没有“销/退”按钮。
    private boolean isShowProductDate;//是否显示生产日期
    private boolean isShowProductDateSelect;//当isShowProductDateSelect==true时，判断是否选择日期选择对话框，当有多个生成批次时需要展示
    private boolean isMustInputNumInSpecif;//云仓的多口味商品只能在子口味中填写数量      是否为订单或者退货订单
    private boolean isModPrice;//是否允许改价:退货单退货订单不受开关控制，都可以改价

    private List<Goods> resultGoodsList;//保存点击购物车后填写的商品，当再一次点击购物车，将加载此数据
    private List<Goods> specifGoods = new ArrayList<>();//子口味商品
    private List<Goods> specifGoodsGive = new ArrayList<>();//子口味商品
    boolean isCal;//判断当前商品是否为父商品且按照子口味计算库存
    private Long orderBillIdFromSaleBill;//临时变量，送货签收模块使用
    private String warehouseName;
    private int tmpBillType;//当前商品所属单据类型，临时变量

    private Long warehouseId;
    private String disPlayProductDate;//当前选中的生产日期用于往SaleBillDetail的productionDate传值  20180301
    private Double pkgQuantity;//大包数量
    private Double origPkgQuantity;//大包数量
    private Double origMidQuantity;//大包数量
    private Double origBaseQuantity;//大包数量
    private Double pkgRealPrice;//大包真正输入大价格
    private Double pkgOrigPrice;//原始价格，从借口获取，离线从价格体系获取，本地不需要处理
    private Double pkgSubAmounte;//大包合计金额
    private String pkgRemark;//大包备注
    private Double pkgGiveQuantity;//大包赠送数量

    private Double midQuantity;//中包数量
    private Double midRealPrice;//中包 AddGoodsDialog中用此 midPriceByGetPrice
    private Double midOrigPrice;
    private Double midSubAmounte;//中包合计金额
    private String midRemark;//中包备注
    private Double midGiveQuantity;//中包赠送数量

    private Double baseQuantity;//小包数量
    private Double baseRealPrice;//小包 AddGoodsDialog中用此 maxPriceByGetPrice
    private Double baseOrigPrice;
    private Double baseSubAmounte;//小包合计金额
    private String baseRemark;//小包备注
    private Double baseGiveQuantity;//小包赠送数量

    private String giveRemark;//赠送备注

    private Double avalibleStockNum;//有效库存，用于子口味库存校验
    private Double realStockNum;//真实库存，用于子口味库存校验

    private String disPlayRealStockStr;//真实库存:名称+数量
    private String disPlayRealStockName;//真实库存:名称
    private String disPlayAvalibleStockStr;//有效库存
    private boolean isOverAvalibleStock;//是否超过库可用数量
    private boolean isOverRealStock;//是否超过实际库存
    private boolean isNegativeStock;//是验证负库存
    private Long sortTimestamp;//用时间戳，作为排序用
    private Double currInputStockNum;//临时记载输入的商品数量

    private String specifyDateOptOrig;
    private String specifyDateValueOrig;
    private Integer acceptAdjustOrig;
    private String occupiedQuantity;

    private String createTime;

    //-----------------------

    //原价
    private String realPrice;

    private String updateTime;

    // --------------- 指导价格 ---------------
    private Double pkgGuidePrice; //大单位指导价

    private Double midGuidePrice; //中单位指导价

    private Double baseGuidePrice; //小单位指导价

    // ---------------当前折扣 ---------------
    private Double pkgDiscount; //大单位折扣

    private Double midDiscount; //中单位折扣

    private Double baseDiscount; //小单位折扣

    // 是否有大单位指导价
    public boolean hasPkgGuidePrice() {
        return pkgGuidePrice != null && pkgGuidePrice > 0;
    }

    // 是否有中单位指导价
    public boolean hasMidGuidePrice() {
        return midGuidePrice != null && midGuidePrice > 0;
    }

    // 是否有小单位指导价
    public boolean hasBaseGuidePrice() {
        return baseGuidePrice != null && baseGuidePrice > 0;
    }

    // 是否有指导价（大中小三个单位有一个就行）
    public boolean hasGuidePrice() {
        return hasPkgGuidePrice() || hasMidGuidePrice() || hasBaseGuidePrice();
    }

    // 根据单位id获取指导价
    public Double getGuidePriceByUnit(String unitId) {
        if (unitId == null) {
            return null;
        }
        if (unitId.startsWith("P")) {
            return pkgGuidePrice;
        } else if (unitId.startsWith("M")) {
            return midGuidePrice;
        } else if (unitId.startsWith("B")) {
            return baseGuidePrice;
        } else {
            return null;
        }
    }

    // 根据单位id判断是否有指导价
    public boolean hasGuidePriceByUnit(String unitId) {
        Double guidePrice = getGuidePriceByUnit(unitId);
        return guidePrice != null && guidePrice > 0;
    }

    // 根据单位id获取realPrice
    public Double getRealPriceByUnit(String unitId) {
        if (unitId == null) {
            return null;
        }
        if (unitId.startsWith("P")) {
            return pkgRealPrice;
        } else if (unitId.startsWith("M")) {
            return midRealPrice;
        } else if (unitId.startsWith("B")) {
            return baseRealPrice;
        } else {
            return null;
        }
    }

    public Double getCheapestByUnit(String unitId) {
        if (unitId == null) {
            return null;
        }
        if (unitId.startsWith("P")) {
            return pkgCheapest;
        } else if (unitId.startsWith("M")) {
            return midCheapest;
        } else if (unitId.startsWith("B")) {
            return baseCheapest;
        } else {
            return null;
        }
    }

    // 给指定单位设置价格
    public void setRealPriceByUnit(Double price, String unitId) {
        if (unitId == null) {
            return;
        }
        if (unitId.startsWith("P")) {
            setPkgRealPrice(price);
        } else if (unitId.startsWith("M")) {
            setMidRealPrice(price);
        } else if (unitId.startsWith("B")) {
            setBaseRealPrice(price);
        }
    }


    public Goods() {
        super();
    }

    public void toObject(JSONObject o) throws Exception {
//        this.setId(JsonUtils.getLong(o, "id", 0L));
//        this.setName(JsonUtils.getString(o, "name", ""));
//
//        //新增订货品批次id
//        this.setPreOrderBillDetailId(JsonUtils.getLong(o, "preOrderBillDetailId", 0L));
//        //新增订货品批次编号
//        this.setPreOrderBillNo(JsonUtils.getString(o, "preOrderBillNo", ""));
//
//        this.setPreOrderBillId(JsonUtils.getLong(o, "preOrderBillId", 0L));
//
//        this.setShortName(JsonUtils.getString(o, "shortName", ""));
//        if (JsonUtils.hasProperty(o, "type")) {
//            JSONObject typeJson = o.getJSONObject("type");
//            GoodsType type = new GoodsType();
//            type.setId(JsonUtils.getLong(typeJson, "id", 0L));
//            type.setTypeChain(JsonUtils.getString(typeJson, "typeChain", ""));
//            type.setName(JsonUtils.getString(typeJson, "name", ""));
//            this.setType(type);
//        }
//        if (JsonUtils.hasProperty(o, "brand")) {
//            JSONObject brandJson = o.getJSONObject("brand");
//            Brand brand = new Brand();
//            brand.setId(JsonUtils.getLong(brandJson, "id", 0L));
//            brand.setName(JsonUtils.getString(brandJson, "name", ""));
//            this.setBrand(brand);
//        }
//        if (JsonUtils.hasProperty(o, "stock")) {
//            JSONObject stockJson = o.getJSONObject("stock");
//            Gson gson = new Gson();
//            this.setStock(gson.fromJson(stockJson.toString(), Stock.class));
//        }
//        this.setDiscount(JsonUtils.getBoolean(o, "discount", true));
//        this.setState(JsonUtils.getByte(o, "state", 0));
//        this.setBaseUnitId(JsonUtils.getString(o, "baseUnitId", ""));
//        this.setBaseUnitName(JsonUtils.getString(o, "baseUnitName", ""));
//        this.setBaseBarcode(JsonUtils.getString(o, "baseBarcode", ""));
//        this.setPkgUnitId(JsonUtils.getString(o, "pkgUnitId", ""));
//        this.setPkgUnitName(JsonUtils.getString(o, "pkgUnitName", ""));
//        this.setPkgBarcode(JsonUtils.getString(o, "pkgBarcode", ""));
//        this.setUnitFactor(JsonUtils.getDouble(o, "unitFactor"));
//        this.setUnitFactorName(JsonUtils.getString(o, "unitFactorName", ""));
//        this.setBaseWholesale(JsonUtils.getDouble(o, "baseWholesale"));
//        this.setBaseRealPrice(JsonUtils.getDouble(o, "baseWholesale"));
//        this.setBaseRetail(JsonUtils.getDouble(o, "baseRetail", 0D));
//        this.setBaseCheapest(JsonUtils.getDouble(o, "baseCheapest", 0D));
//        this.setBasePurchase(JsonUtils.getDouble(o, "basePurchase", 0D));
//        this.setBaseSpecials1(JsonUtils.getDouble(o, "baseSpecials1", 0D));
//        this.setBaseSpecials2(JsonUtils.getDouble(o, "baseSpecials2", 0D));
//        this.setBaseSpecials3(JsonUtils.getDouble(o, "baseSpecials3", 0D));
//        this.setPkgWholesale(JsonUtils.getDouble(o, "pkgWholesale"));
//        this.setPkgRealPrice(JsonUtils.getDouble(o, "pkgWholesale"));
//
//        this.setPkgRetail(JsonUtils.getDouble(o, "pkgRetail", 0D));
//        this.setPkgCheapest(JsonUtils.getDouble(o, "pkgCheapest", 0D));
//        this.setPkgPurchase(JsonUtils.getDouble(o, "pkgPurchase", 0D));
//        this.setPkgSpecials1(JsonUtils.getDouble(o, "pkgSpecials1", 0D));
//        this.setPkgSpecials2(JsonUtils.getDouble(o, "pkgSpecials2", 0D));
//        this.setPkgSpecials3(JsonUtils.getDouble(o, "pkgSpecials3", 0D));
//        this.setCostPrice(JsonUtils.getDouble(o, "costPrice", 0D));
//        this.setArticleNumber(JsonUtils.getString(o, "articleNumber", ""));
//        this.setSpecifications(JsonUtils.getString(o, "specifications", ""));
//        this.setSeq(JsonUtils.getInt(o, "seq", 0));
//        this.setOrigPlace(JsonUtils.getString(o, "origPlace", ""));
//        this.setSupplierId(JsonUtils.getLong(o, "supplierId", 0L));
//        this.setOtherBarcode(JsonUtils.getString(o, "otherBarcode", ""));
//        this.setOtherBarcode1(JsonUtils.getString(o, "otherBarcode1", ""));
//        this.setOtherBarcode2(JsonUtils.getString(o, "otherBarcode2", ""));
//        this.setStatisticsId(JsonUtils.getString(o, "statisticsId", ""));
//        this.setWarnDays(JsonUtils.getInt(o, "warnDays", 0));
//        this.setShelfLife(JsonUtils.getInt(o, "shelfLife", 0));
//        this.setPicture(JsonUtils.getString(o, "picture", ""));
//        this.setCreateTime(JsonUtils.getString(o, "createTime", ""));
//        this.setUpdateTime(JsonUtils.getString(o, "createTime", ""));
//        this.setSpecifications1(JsonUtils.getString(o, "specifications1", ""));
//        this.setParentState(JsonUtils.getInt(o, "parentState", Constants.INT_DEFAULT_VALUE));
//        this.setParentId(JsonUtils.getLong(o, "parentId", Constants.LONG_DEFAULT_VALUE));
//        this.setProductionDateState(JsonUtils.getInt(o, "productionDateState", 0));
//        this.setHasBillRecord(JsonUtils.getInt(o, "hasBillRecord", 0));
//
//        //mid
//        this.setMidUnitId(JsonUtils.getString(o, "midUnitId", ""));
//        this.setMidUnitFactor(JsonUtils.getDouble(o, "midUnitFactor"));
//        this.setMidUnitName(JsonUtils.getString(o, "midUnitName", ""));
//        this.setMidBarcode(JsonUtils.getString(o, "midBarcode", ""));
//        this.setMidWholesale(JsonUtils.getDouble(o, "midWholesale"));
//        this.setMidRealPrice(JsonUtils.getDouble(o, "midWholesale"));
//
//        this.setMidRetail(JsonUtils.getDouble(o, "midRetail", 0D));
//        this.setMidCheapest(JsonUtils.getDouble(o, "midCheapest", 0D));
//        this.setMidPurchase(JsonUtils.getDouble(o, "midPurchase", 0D));
//        this.setMidSpecials1(JsonUtils.getDouble(o, "midSpecials1", 0D));
//        this.setMidSpecials2(JsonUtils.getDouble(o, "midSpecials2", 0D));
//        this.setMidSpecials3(JsonUtils.getDouble(o, "midSpecials3", 0D));
//
//        //taste
//        this.setCalStockByTaste(JsonUtils.getInt(o, "calStockByTaste", 1));
//        this.setTasteBarcode(JsonUtils.getString(o, "tasteBarcode", ""));
//
//        this.setPicture1(JsonUtils.getString(o, "picture1", ""));
//        this.setPicture2(JsonUtils.getString(o, "picture2", ""));
//        this.setPicture3(JsonUtils.getString(o, "picture3", ""));
    }

    public Double getPkgGuidePrice() {
        return pkgGuidePrice;
    }

    public void setPkgGuidePrice(Double pkgGuidePrice) {
        this.pkgGuidePrice = pkgGuidePrice;
    }

    public Double getMidGuidePrice() {
        return midGuidePrice;
    }

    public void setMidGuidePrice(Double midGuidePrice) {
        this.midGuidePrice = midGuidePrice;
    }

    public Double getBaseGuidePrice() {
        return baseGuidePrice;
    }

    public void setBaseGuidePrice(Double baseGuidePrice) {
        this.baseGuidePrice = baseGuidePrice;
    }

    public Double getPkgDiscount() {
        return pkgDiscount;
    }

    public void setPkgDiscount(Double pkgDiscount) {
        this.pkgDiscount = pkgDiscount;
    }

    public Double getMidDiscount() {
        return midDiscount;
    }

    public void setMidDiscount(Double midDiscount) {
        this.midDiscount = midDiscount;
    }

    public Double getBaseDiscount() {
        return baseDiscount;
    }

    public void setBaseDiscount(Double baseDiscount) {
        this.baseDiscount = baseDiscount;
    }

    /**
     * 是否不分口味核算库存
     *
     * @return
     */
    public boolean isCalStockByTast() {
        //父商品
        if (id != null && (getParentId() == null || id == getParentId())) {
            return calStockByTaste == 0;
            //子商品
        } else {
            return calStockByTaste == 1;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Goods goods = (Goods) o;
        return Objects.equals(id, goods.id) &&
                Objects.equals(cid, goods.cid) &&
                Objects.equals(name, goods.name);
    }

    @SuppressLint("NewApi")
    @Override
    public int hashCode() {

        return Objects.hash(id, cid, name);
    }


    public Long getPreOrderBillId() {
        return preOrderBillId;
    }

    public void setPreOrderBillId(Long preOrderBillId) {
        this.preOrderBillId = preOrderBillId;
    }

    public String getPreOrderBillNo() {
        return preOrderBillNo;
    }

    public void setPreOrderBillNo(String preOrderBillNo) {
        this.preOrderBillNo = preOrderBillNo;
    }

    public Long getPreOrderBillDetailId() {
        return preOrderBillDetailId;
    }

    public void setPreOrderBillDetailId(Long preOrderBillDetailId) {
        this.preOrderBillDetailId = preOrderBillDetailId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public void setDiscount(Boolean discount) {
        this.discount = discount;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getBaseUnitId() {
        return baseUnitId;
    }

    public void setBaseUnitId(String baseUnitId) {
        this.baseUnitId = baseUnitId;
    }

    public String getBaseUnitName() {
        return baseUnitName;
    }

    public void setBaseUnitName(String baseUnitName) {
        this.baseUnitName = baseUnitName;
    }

    public String getBaseBarcode() {
        return baseBarcode;
    }

    public void setBaseBarcode(String baseBarcode) {
        this.baseBarcode = baseBarcode;
    }

    public String getPkgUnitId() {
        return pkgUnitId;
    }

    public void setPkgUnitId(String pkgUnitId) {
        this.pkgUnitId = pkgUnitId;
    }

    public String getPkgUnitName() {
        return pkgUnitName;
    }

    public void setPkgUnitName(String pkgUnitName) {
        this.pkgUnitName = pkgUnitName;
    }

    public String getPkgBarcode() {
        return pkgBarcode;
    }

    public void setPkgBarcode(String pkgBarcode) {
        this.pkgBarcode = pkgBarcode;
    }

    public Double getUnitFactor() {
        return unitFactor;
    }

    public void setUnitFactor(Double unitFactor) {
        this.unitFactor = unitFactor;
    }

    public String getUnitFactorName() {
        return unitFactorName;
    }

    public void setUnitFactorName(String unitFactorName) {
        this.unitFactorName = unitFactorName;
    }

    public Double getBaseWholesale() {
        return baseWholesale;
    }

    public void setBaseWholesale(Double baseWholesale) {
        this.baseWholesale = baseWholesale;
    }

    public Double getBaseRetail() {
        return baseRetail;
    }

    public void setBaseRetail(Double baseRetail) {
        this.baseRetail = baseRetail;
    }

    public Double getBaseCheapest() {
        return baseCheapest;
    }

    public void setBaseCheapest(Double baseCheapest) {
        this.baseCheapest = baseCheapest;
    }

    public Double getBasePurchase() {
        return basePurchase;
    }

    public void setBasePurchase(Double basePurchase) {
        this.basePurchase = basePurchase;
    }

    public Double getBaseSpecials1() {
        return baseSpecials1;
    }

    public void setBaseSpecials1(Double baseSpecials1) {
        this.baseSpecials1 = baseSpecials1;
    }

    public Double getBaseSpecials2() {
        return baseSpecials2;
    }

    public void setBaseSpecials2(Double baseSpecials2) {
        this.baseSpecials2 = baseSpecials2;
    }

    public Double getBaseSpecials3() {
        return baseSpecials3;
    }

    public void setBaseSpecials3(Double baseSpecials3) {
        this.baseSpecials3 = baseSpecials3;
    }

    public Double getPkgWholesale() {
        return pkgWholesale;
    }

    public void setPkgWholesale(Double pkgWholesale) {
        this.pkgWholesale = pkgWholesale;
    }

    public Double getPkgRetail() {
        return pkgRetail;
    }

    public void setPkgRetail(Double pkgRetail) {
        this.pkgRetail = pkgRetail;
    }

    public Double getPkgCheapest() {
        return pkgCheapest;
    }

    public void setPkgCheapest(Double pkgCheapest) {
        this.pkgCheapest = pkgCheapest;
    }

    public Double getPkgPurchase() {
        return pkgPurchase;
    }

    public void setPkgPurchase(Double pkgPurchase) {
        this.pkgPurchase = pkgPurchase;
    }

    public Double getPkgSpecials1() {
        return pkgSpecials1;
    }

    public void setPkgSpecials1(Double pkgSpecials1) {
        this.pkgSpecials1 = pkgSpecials1;
    }

    public Double getPkgSpecials2() {
        return pkgSpecials2;
    }

    public void setPkgSpecials2(Double pkgSpecials2) {
        this.pkgSpecials2 = pkgSpecials2;
    }

    public Double getPkgSpecials3() {
        return pkgSpecials3;
    }

    public void setPkgSpecials3(Double pkgSpecials3) {
        this.pkgSpecials3 = pkgSpecials3;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getOrigPlace() {
        return origPlace;
    }

    public void setOrigPlace(String origPlace) {
        this.origPlace = origPlace;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getOtherBarcode() {
        return otherBarcode;
    }

    public void setOtherBarcode(String otherBarcode) {
        this.otherBarcode = otherBarcode;
    }

    public String getOtherBarcode1() {
        return otherBarcode1;
    }

    public void setOtherBarcode1(String otherBarcode1) {
        this.otherBarcode1 = otherBarcode1;
    }

    public String getOtherBarcode2() {
        return otherBarcode2;
    }

    public void setOtherBarcode2(String otherBarcode2) {
        this.otherBarcode2 = otherBarcode2;
    }

    public String getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(String statisticsId) {
        this.statisticsId = statisticsId;
    }

    public Integer getWarnDays() {
        return warnDays;
    }

    public void setWarnDays(Integer warnDays) {
        this.warnDays = warnDays;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getSpecifications1() {
        return specifications1;
    }

    public void setSpecifications1(String specifications1) {
        this.specifications1 = specifications1;
    }

    public Integer getParentState() {
        return parentState;
    }

    public void setParentState(Integer parentState) {
        this.parentState = parentState;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getProductionDateState() {
        return productionDateState;
    }

    public void setProductionDateState(Integer productionDateState) {
        this.productionDateState = productionDateState;
    }

    public String getMidUnitId() {
        return midUnitId;
    }

    public void setMidUnitId(String midUnitId) {
        this.midUnitId = midUnitId;
    }

    public Double getMidUnitFactor() {
        return midUnitFactor;
    }

    public void setMidUnitFactor(Double midUnitFactor) {
        this.midUnitFactor = midUnitFactor;
    }

    public String getMidUnitName() {
        return midUnitName;
    }

    public void setMidUnitName(String midUnitName) {
        this.midUnitName = midUnitName;
    }

    public String getMidBarcode() {
        return midBarcode;
    }

    public void setMidBarcode(String midBarcode) {
        this.midBarcode = midBarcode;
    }

    public Double getMidWholesale() {
        return midWholesale;
    }

    public void setMidWholesale(Double midWholesale) {
        this.midWholesale = midWholesale;
    }

    public Double getMidRetail() {
        return midRetail;
    }

    public void setMidRetail(Double midRetail) {
        this.midRetail = midRetail;
    }

    public Double getMidCheapest() {
        return midCheapest;
    }

    public void setMidCheapest(Double midCheapest) {
        this.midCheapest = midCheapest;
    }

    public Double getMidPurchase() {
        return midPurchase;
    }

    public void setMidPurchase(Double midPurchase) {
        this.midPurchase = midPurchase;
    }

    public Double getMidSpecials1() {
        return midSpecials1;
    }

    public void setMidSpecials1(Double midSpecials1) {
        this.midSpecials1 = midSpecials1;
    }

    public Double getMidSpecials2() {
        return midSpecials2;
    }

    public void setMidSpecials2(Double midSpecials2) {
        this.midSpecials2 = midSpecials2;
    }

    public Double getMidSpecials3() {
        return midSpecials3;
    }

    public void setMidSpecials3(Double midSpecials3) {
        this.midSpecials3 = midSpecials3;
    }

    public Integer getCalStockByTaste() {
        return calStockByTaste;
    }

    public void setCalStockByTaste(Integer calStockByTaste) {
        this.calStockByTaste = calStockByTaste;
    }

    public String getTasteBarcode() {
        return tasteBarcode;
    }

    public void setTasteBarcode(String tasteBarcode) {
        this.tasteBarcode = tasteBarcode;
    }

    public Integer getHasBillRecord() {
        return hasBillRecord;
    }

    public void setHasBillRecord(Integer hasBillRecord) {
        this.hasBillRecord = hasBillRecord;
    }

    public List<Goods> getSubList() {
        return subList;
    }

    public void setSubList(List<Goods> subList) {
        this.subList = subList;
    }

    public Integer getTypeSelect() {
        return typeSelect;
    }

    public void setTypeSelect(Integer typeSelect) {
        this.typeSelect = typeSelect;
    }

    public Integer getTypeInput() {
        return typeInput;
    }

    public void setTypeInput(Integer typeInput) {
        this.typeInput = typeInput;
    }

    public Integer getOriginaTypeInput() {
        return originaTypeInput;
    }

    public void setOriginaTypeInput(Integer originaTypeInput) {
        this.originaTypeInput = originaTypeInput;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public boolean isShowGive() {
        return isShowGive;
    }

    public void setShowGive(boolean showGive) {
        isShowGive = showGive;
    }

    public boolean isShowSaleReject() {
        return isShowSaleReject;
    }

    public void setShowSaleReject(boolean showSaleReject) {
        isShowSaleReject = showSaleReject;
    }

    public boolean isShowProductDate() {
        return isShowProductDate;
    }

    public void setShowProductDate(boolean showProductDate) {
        isShowProductDate = showProductDate;
    }

    public boolean isShowProductDateSelect() {
        return isShowProductDateSelect;
    }

    public void setShowProductDateSelect(boolean showProductDateSelect) {
        isShowProductDateSelect = showProductDateSelect;
    }

    public boolean isMustInputNumInSpecif() {
        return isMustInputNumInSpecif;
    }

    public void setMustInputNumInSpecif(boolean mustInputNumInSpecif) {
        isMustInputNumInSpecif = mustInputNumInSpecif;
    }

    public boolean isModPrice() {
        return isModPrice;
    }

    public void setModPrice(boolean modPrice) {
        isModPrice = modPrice;
    }

    public List<Goods> getResultGoodsList() {
        return resultGoodsList;
    }

    public void setResultGoodsList(List<Goods> resultGoodsList) {
        this.resultGoodsList = resultGoodsList;
    }

    public List<Goods> getSpecifGoods() {
        return specifGoods;
    }

    public void setSpecifGoods(List<Goods> specifGoods) {
        this.specifGoods = specifGoods;
    }

    public List<Goods> getSpecifGoodsGive() {
        return specifGoodsGive;
    }

    public void setSpecifGoodsGive(List<Goods> specifGoodsGive) {
        this.specifGoodsGive = specifGoodsGive;
    }

    public boolean isCal() {
        return isCal;
    }

    public void setCal(boolean cal) {
        isCal = cal;
    }

    public Long getOrderBillIdFromSaleBill() {
        return orderBillIdFromSaleBill;
    }

    public void setOrderBillIdFromSaleBill(Long orderBillIdFromSaleBill) {
        this.orderBillIdFromSaleBill = orderBillIdFromSaleBill;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getTmpBillType() {
        return tmpBillType;
    }

    public void setTmpBillType(int tmpBillType) {
        this.tmpBillType = tmpBillType;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getDisPlayProductDate() {
        return disPlayProductDate;
    }

    public void setDisPlayProductDate(String disPlayProductDate) {
        this.disPlayProductDate = disPlayProductDate;
    }

    public Double getPkgQuantity() {
        return pkgQuantity;
    }

    public void setPkgQuantity(Double pkgQuantity) {
        this.pkgQuantity = pkgQuantity;
    }

    public Double getOrigPkgQuantity() {
        return origPkgQuantity;
    }

    public void setOrigPkgQuantity(Double origPkgQuantity) {
        this.origPkgQuantity = origPkgQuantity;
    }

    public Double getOrigMidQuantity() {
        return origMidQuantity;
    }

    public void setOrigMidQuantity(Double origMidQuantity) {
        this.origMidQuantity = origMidQuantity;
    }

    public Double getOrigBaseQuantity() {
        return origBaseQuantity;
    }

    public void setOrigBaseQuantity(Double origBaseQuantity) {
        this.origBaseQuantity = origBaseQuantity;
    }

    public Double getPkgRealPrice() {
        return pkgRealPrice;
    }

    public void setPkgRealPrice(Double pkgRealPrice) {
        this.pkgRealPrice = pkgRealPrice;
        if (pkgGuidePrice != null && pkgGuidePrice > 0 && pkgRealPrice != null) {
            setPkgDiscount(pkgRealPrice / pkgGuidePrice);
        } else {
            setPkgDiscount(null);
        }
    }

    public Double getPkgOrigPrice() {
        return pkgOrigPrice;
    }

    public void setPkgOrigPrice(Double pkgOrigPrice) {
        this.pkgOrigPrice = pkgOrigPrice;
    }

    public Double getPkgSubAmounte() {
        return pkgSubAmounte;
    }

    public void setPkgSubAmounte(Double pkgSubAmounte) {
        this.pkgSubAmounte = pkgSubAmounte;
    }

    public String getPkgRemark() {
        return pkgRemark;
    }

    public void setPkgRemark(String pkgRemark) {
        this.pkgRemark = pkgRemark;
    }

    public Double getPkgGiveQuantity() {
        return pkgGiveQuantity;
    }

    public void setPkgGiveQuantity(Double pkgGiveQuantity) {
        this.pkgGiveQuantity = pkgGiveQuantity;
    }

    public Double getMidQuantity() {
        return midQuantity;
    }

    public void setMidQuantity(Double midQuantity) {
        this.midQuantity = midQuantity;
    }

    public Double getMidRealPrice() {
        return midRealPrice;
    }

    public void setMidRealPrice(Double midRealPrice) {
        this.midRealPrice = midRealPrice;
        if (midGuidePrice != null && midGuidePrice > 0 && midRealPrice != null) {
            setMidDiscount(midRealPrice / midGuidePrice);
        } else {
            setMidDiscount(null);
        }
    }

    public Double getMidOrigPrice() {
        return midOrigPrice;
    }

    public void setMidOrigPrice(Double midOrigPrice) {
        this.midOrigPrice = midOrigPrice;
    }

    public Double getMidSubAmounte() {
        return midSubAmounte;
    }

    public void setMidSubAmounte(Double midSubAmounte) {
        this.midSubAmounte = midSubAmounte;
    }

    public String getMidRemark() {
        return midRemark;
    }

    public void setMidRemark(String midRemark) {
        this.midRemark = midRemark;
    }

    public Double getMidGiveQuantity() {
        return midGiveQuantity;
    }

    public void setMidGiveQuantity(Double midGiveQuantity) {
        this.midGiveQuantity = midGiveQuantity;
    }

    public Double getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(Double baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public Double getBaseRealPrice() {
        return baseRealPrice;
    }

    public void setBaseRealPrice(Double baseRealPrice) {
        this.baseRealPrice = baseRealPrice;
        if (baseGuidePrice != null && baseGuidePrice > 0 && baseRealPrice != null) {
            setBaseDiscount(baseRealPrice / baseGuidePrice);
        } else {
            setBaseDiscount(null);
        }
    }

    public Double getBaseOrigPrice() {
        return baseOrigPrice;
    }

    public void setBaseOrigPrice(Double baseOrigPrice) {
        this.baseOrigPrice = baseOrigPrice;
    }

    public Double getBaseSubAmounte() {
        return baseSubAmounte;
    }

    public void setBaseSubAmounte(Double baseSubAmounte) {
        this.baseSubAmounte = baseSubAmounte;
    }

    public String getBaseRemark() {
        return baseRemark;
    }

    public void setBaseRemark(String baseRemark) {
        this.baseRemark = baseRemark;
    }

    public Double getBaseGiveQuantity() {
        return baseGiveQuantity;
    }

    public void setBaseGiveQuantity(Double baseGiveQuantity) {
        this.baseGiveQuantity = baseGiveQuantity;
    }

    public String getGiveRemark() {
        return giveRemark;
    }

    public void setGiveRemark(String giveRemark) {
        this.giveRemark = giveRemark;
    }

    public Double getAvalibleStockNum() {
        return avalibleStockNum;
    }

    public void setAvalibleStockNum(Double avalibleStockNum) {
        this.avalibleStockNum = avalibleStockNum;
    }

    public Double getRealStockNum() {
        return realStockNum;
    }

    public void setRealStockNum(Double realStockNum) {
        this.realStockNum = realStockNum;
    }

    public String getDisPlayRealStockStr() {
        return disPlayRealStockStr;
    }

    public void setDisPlayRealStockStr(String disPlayRealStockStr) {
        this.disPlayRealStockStr = disPlayRealStockStr;
    }

    public String getDisPlayRealStockName() {
        return disPlayRealStockName;
    }

    public void setDisPlayRealStockName(String disPlayRealStockName) {
        this.disPlayRealStockName = disPlayRealStockName;
    }

    public String getDisPlayAvalibleStockStr() {
        return disPlayAvalibleStockStr;
    }

    public void setDisPlayAvalibleStockStr(String disPlayAvalibleStockStr) {
        this.disPlayAvalibleStockStr = disPlayAvalibleStockStr;
    }

    public boolean isOverAvalibleStock() {
        return isOverAvalibleStock;
    }

    public void setOverAvalibleStock(boolean overAvalibleStock) {
        isOverAvalibleStock = overAvalibleStock;
    }

    public boolean isOverRealStock() {
        return isOverRealStock;
    }

    public void setOverRealStock(boolean overRealStock) {
        isOverRealStock = overRealStock;
    }

    public boolean isNegativeStock() {
        return isNegativeStock;
    }

    public void setNegativeStock(boolean negativeStock) {
        isNegativeStock = negativeStock;
    }

    public Long getSortTimestamp() {
        return sortTimestamp;
    }

    public void setSortTimestamp(Long sortTimestamp) {
        this.sortTimestamp = sortTimestamp;
    }

    public Double getCurrInputStockNum() {
        return currInputStockNum;
    }

    public void setCurrInputStockNum(Double currInputStockNum) {
        this.currInputStockNum = currInputStockNum;
    }

    public String getSpecifyDateOptOrig() {
        return specifyDateOptOrig;
    }

    public void setSpecifyDateOptOrig(String specifyDateOptOrig) {
        this.specifyDateOptOrig = specifyDateOptOrig;
    }

    public String getSpecifyDateValueOrig() {
        return specifyDateValueOrig;
    }

    public void setSpecifyDateValueOrig(String specifyDateValueOrig) {
        this.specifyDateValueOrig = specifyDateValueOrig;
    }

    public Integer getAcceptAdjustOrig() {
        return acceptAdjustOrig;
    }

    public void setAcceptAdjustOrig(Integer acceptAdjustOrig) {
        this.acceptAdjustOrig = acceptAdjustOrig;
    }

    public String getOccupiedQuantity() {
        return occupiedQuantity;
    }

    public void setOccupiedQuantity(String occupiedQuantity) {
        this.occupiedQuantity = occupiedQuantity;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public Goods clone() {
        try {
            return (Goods) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}