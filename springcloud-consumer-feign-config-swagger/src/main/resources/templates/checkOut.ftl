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
                <h1 class="text-center">101离店结账</h1>
                <form action="#" method="post">
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="lableText">手机号</label>
                            <br/>
                            <label class="lableText">15027861733449</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label for="phone" class="lableText">离店时间</label>
                            <input type="text" id="phone" class="form-control Wdate" onfocus="WdatePicker({lang:'zh-cn'})">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                           &nbsp;
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <input type="submit" value="结账" class="btn btn-primary">
                            <input type="submit" value="退卡" class="btn btn-primary">
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