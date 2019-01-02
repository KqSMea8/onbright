<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="zh">
  <head>
    <title>昂宝酒店</title>
    <base id="base" href="${base}">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400,700,900|Rubik:300,400,700" rel="stylesheet">
    <link rel="stylesheet" href="../css/bootstrap.css">
    <link rel="stylesheet" href="../css/animate.css">
    <link rel="stylesheet" href="../css/owl.carousel.min.css">
    <link rel="stylesheet" href="../fonts/ionicons/css/ionicons.min.css">
    <link rel="stylesheet" href="../fonts/fontawesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="../css/magnific-popup.css">
    <link rel="stylesheet" href="../css/style.css">
    <style>
        .lableText {
            color : white;
        }
        li{
            float:left;
            width: 110px;
            height: 100px;
            margin: 15px;
            text-align: center;
        }
        .semScroll{
            overflow: scroll;
            padding-top: 100px;
        }
    </style>
  </head>
  <body>
    
    <header role="banner">
      <nav class="navbar navbar-expand-md navbar-dark bg-light">
        <div class="container">
          <a id="hotalName" class="navbar-brand" href="index.html">OB Hotel</a>
          <div class="collapse navbar-collapse navbar-light" id="navbarsExample05">
            <ul class="navbar-nav ml-auto pl-lg-5 pl-0">
              <li class="nav-item">
                <a id="fontPage" class="nav-link active" href="${base}/hotelweb/login">首页</a>
              </li>
              <li class="nav-item">
                <a id="roomPage" class="nav-link active" href="${base}/hotelweb/login">房态</a>
              </li>
              <li class="nav-item">
                 <a id="setupPage" class="nav-link active" href="${base}/hotelweb/login">设置</a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </header>
    <!-- loader -->
    <div id="loader" class="show fullscreen"><svg class="circular" width="48px" height="48px"><circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee"/><circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#f4b214"/></svg></div>
    <script src="../js/jquery-3.2.1.min.js"></script>
    <script src="../js/jquery-migrate-3.0.0.js"></script>
    <script src="../js/popper.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/owl.carousel.min.js"></script>
    <script src="../js/jquery.waypoints.min.js"></script>
    <script src="../js/jquery.stellar.min.js"></script>
    <script src="../js/jquery.magnific-popup.min.js"></script>
    <script src="../js/magnific-popup-options.js"></script>
    <script src="../js/main.js"></script>
    <script src="../js/WdatePicker.js"></script>
    <script src="../js/calendar.js"></script>
  </body>
</html>