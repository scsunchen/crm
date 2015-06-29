
<!DOCTYPE html>
<html>
<head>
  <title>${title}</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <!-- Bootstrap core CSS -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/main.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button"
              class="navbar-toggle"
              data-toggle="collapse"
              data-target="#navbar"
              aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
      <ul class="nav navbar-nav navbar-right">
        <li class="active"><a href="#">${module-name}</a></li>
        <li><a href="#">${module-name}</a></li>
        <li><a href="#">${module-name}</a></li>
        <li><a href="#">${module-name}</a></li>
        <li><a href="#">${module-name}</a></li>
        <li><a href="#">${module-name}</a></li>
        <li><a href="#">logout ${username}</a></li>
      </ul>
    </div>
  </div>
</nav>
<div class="container-fluid">
  <div class="row row-offcanvas-left">
    <div class="col-xs-12 col-sm-6 col-md-2 sidebar-offcanvas">
      <div class="well well-sm"
           style="word-wrap: break-word;">
        <h4>${module-feature-group}</h4>
        <ul>
          <li><a href="#" >${feature}</a></li>
          <li ><a href="#">${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
        </ul>
        <h4>${module-feature-group}</h4>
        <ul>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
          <li><a href="#" >${feature}</a></li>
        </ul>
      </div>
    </div>
    <div class="col-xs-12 col-sm-12 col-md-10 main">
      <div class="panel panel-default">
        <div class="panel-heading">${title}</div>
        <div class="panel-body">
          <div class="row">
            <p class="pull-left visible-xs visible-sm">
              <button type="button"
                      class="btn btn-sm btn-default"
                      data-toggle="offcanvas"><span class="glyphicon glyphicon-menu-right"></span> meni</button>
            </p>
          </div>
          <form>
            <div class="form-group">
              <label for="exampleInputEmail1">Email address</label>
              <input type="email" class="form-control" id="exampleInputEmail1" >
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Password</label>
              <input type="password" class="form-control" id="exampleInputPassword1" >
            </div>
            <br/>
            <br/>
            <div class="form-group">
              <button type="submit" class="btn btn-primary">Dodaj</button>

            </div>
            <div class="table-responsive">
              <table class="table">
                <thead>
                <tr>
                  <th></th>
                  <th>Korisničko ime</th>
                  <th>Lozinka</th>
                  <th>Ime i prezime</th>
                  <th>Nivo ovlašćenja</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                  <td><div class="btn-group btn-group-sm" role="group" >
                    <button class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> pregled</button>
                    <button class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> brisanje</button>

                  </div></td>
                  <td>admin</td>
                  <td>*****</td>
                  <td>admin</td>
                  <td>ADMINISTRATOR</td>
                </tr>
                <tr>
                  <td><div class="btn-group btn-group-sm" role="group" >
                    <button class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> pregled</button>
                    <button class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span> brisanje</button>

                  </div></td>
                  <td>admin1</td>
                  <td>*****</td>
                  <td>admin1</td>
                  <td>ADMINISTRATOR</td>
                </tr>
              </table>
            </div>

            <nav>
              <ul class="pager" style="text-align: right;">
                <li class="disabled"><a href="#"><span class="glyphicon glyphicon-backward"></span> Prethodna strana</a></li>
                <li class="disabled"><a href="#"><span class="glyphicon glyphicon-forward"></span> Naredna strana</a></li>
              </ul>
            </nav>
            <div class="form-group">
              <button type="submit" class="btn btn-primary">Snimi</button>
              <button type="submit" class="btn btn-default">Nazad</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="js/jquery-1.11.2.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
  $(document).ready(function () {
    $('[data-toggle="offcanvas"]').click(function () {
      $('.row-offcanvas-left').toggleClass('active')
    });
  });
</script>
</body>
</html>