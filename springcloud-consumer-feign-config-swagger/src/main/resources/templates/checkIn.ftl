<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="zh">
  <body>
    <#include "/header.ftl">
    <!-- END header -->
    <section class="site-hero overlay" data-stellar-background-ratio="0.5" style="background-image: url(../images/big_image_1.png);">
      <div class="container">
        <div class="row align-items-center site-hero-inner justify-content-center">
            <div class="col-md-6">
                <h1 class="text-center">101入住登记</h1>
                <form action="#" method="post">
                    <div class="row">
                        <div class="col-md-8 form-group">
                            <label for="name" class="lableText">手机号</label>
                            <input type="text" id="name" class="form-control ">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8 form-group">
                            <label for="phone" class="lableText">入住时间</label>
                            <input type="text" id="phone" class="form-control Wdate" onfocus="WdatePicker({lang:'zh-cn'})">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8 form-group">
                            <label for="phone" class="lableText">离店时间</label>
                            <input type="text" id="phone" class="form-control Wdate" onfocus="WdatePicker({lang:'zh-cn'})">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-10 form-group">
                            <input type="submit" value="登记" class="btn btn-primary">
                            <input type="submit" value="发卡(2)" class="btn btn-primary">
                            <input type="submit" value="门锁密码" class="btn btn-primary">
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-md-6">
                <img src="../images/img_4.jpg" alt="" class="img-fluid">
            </div>
        </div>
    </section>
    <!-- END section -->
    <#include "/footer.ftl">
  </body>
</html>