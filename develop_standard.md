
## 团队合作Api协助管理工具，拒绝word文档
[ApiZza官方地址](https://apizza.net/) 使用简单，支持团队合作

## 后台返回数据结构体规范

返回体通用Json格式，以Java为例

 ```
 class Response<T>{
    //操作状态
    int code;
    String msg;

    //单个返回体
    T data;

    //数组返回体
    List<T> dataList;
    int totals
 }
 ```
* 状态说明：查询，添加等操作成功 code=1 msg="不用返回"</br>
    查询，添加等操作失败 code=0 msg="返回失败原因"，msg用作统一提示</br>
    针对特殊状态code可以定义为固定值，例如code=-1表示登录失败
* T 是java中的泛型表示，代表任意结构
* T 代表单个返回体，表示任意数据 ，对应key是 data
* List<T> 代表多个返回体，表示任意数据，对应key是 dataList
* totals 用来表示多个返回体的总数

#### 返回体案例

 ```
 1.单个返回体Json结构：以商品详情为例
    {"code":"1","msg":"成功","data":{"p_id":"商品id","p_name":"商品名称","p_pic":"商品图片"}}
    {"code":"0","msg":"商品id异常","data":{}}

 2.多个返回体Json结构：以商品列表为例
     {"code":"1","msg":"成功","totals":100,"dataList":[{"p_id":"商品id","p_name":"商品名称"},{"p_id":"商品id","p_name":"商品名称"}]}
     {"code":"0","msg":"异常提示","dataList":[]}

 ```

**1、服务端 [必须]() 用 [data]()字段表示单个 ， [dataList]()字段表示多个**</br>
**2、推荐服务端通过静态管理类来统一创建返回体结构**</br>
**3、结构体是通用结构，非特殊情况不准增加多余字段**</br>