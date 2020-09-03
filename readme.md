# 西南闲置app试作
![](mini.png)
## V0.85
### 版本日志
1.改善了一下发布页面。  
2.完成了将输入内容转成json的操作（通过Gson的tojson）.

效果如图：  
![](gif/V0.85.gif)



## V0.8
### 版本日志
1.完成了搜索界面的搜索框功能，通过handler动态刷新界面（不可以使用setArguments）。  
2.完成了发布页面的基本五毛框架设计。  

### 存在问题 
1.token时限和鉴权问题，后端小伙伴说没时间做，只能暂时使用临时token. 

效果如图：  
![](gif/V0.8.gif)

## V0.7
### 版本日志
1.增加了全局搜索的页面（搜索框尚未完成）  
2.制作了LoadingDialog，参考自[https://github.com/gittjy/LoadingDialog](https://github.com/gittjy/LoadingDialog)  
3.修改了小部分界面设计问题  

### 存在问题
1.缓存和实时请求问题，请求速度有点慢，可进行缓存会失去实时性  
2.token时限和鉴权问题  
 
效果如图：    
![](gif/V0.7.gif)

## V0.6
### 版本日志
1.通过后端小伙伴给的接口和token连上了详情页面，通过okhttp和gson抓取数据并渲染到详情页面中（使用了postman进行测试）  
2.加入sharedpreference作为缓存，减少请求次数  
3.参考网上代码,编写了MyImageView,可以通过url链接来异步加载图片（使用hanlder回传主线程），但后续还需要对图片进行压缩或者缓存处理。（[参考地址](https://blog.csdn.net/qq_33200967/article/details/77263062)）  

效果如图：  
![](gif/V0.6.gif)

## V0.5
### 版本日志
1.根据小程序页面，初步绘制出五毛界面  
2.使用okhttp和gson抓取了首页的数据，并完成显示  
3.使用viewpager制作了首页的滑动图片效果  

效果如图：  
![](gif/V0.5.gif)
