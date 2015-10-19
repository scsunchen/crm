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

      <div role="tabpanel">

        <!-- Nav tabs -->
        <ul class="nav nav-tabs" role="tablist">
          <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">grid-form kobinacija</a></li>
          <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">list-form kombinacija</a></li>
          <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">list-form kombinacija</a></li>
          <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">list-form kombinacija</a></li>
          <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">list-form kombinacija</a></li>
        </ul>

        <!-- Tab panes -->
        <div class="tab-content">
          <div role="tabpanel" class="tab-pane active" id="home">
            <form>
              <div class="form-group">
                <label for="exampleInputEmail1">Email address</label>
                <input type="email" class="form-control" id="exampleInputEmail1" >
              </div>
              <div class="form-group">
                <label for="exampleInputPassword1">Password</label>
                <input type="password" class="form-control" id="exampleInputPassword1" >
              </div>
              <div class="form-group">
                <label for="exampleInputFile">File input</label>
                <input type="file" id="exampleInputFile">
                <p class="help-block">Example block-level help text here.</p>
              </div>
              <div class="checkbox"><label><input type="checkbox"> Check me out</label></div>
            </form>
          </div>
          <div role="tabpanel" class="tab-pane" id="profile">
            <div class="list-group">
              <a href="#" class="list-group-item">
                <h4 class="list-group-item-heading">text</h4>
                <p class="list-group-item-text">opis</p>
              </a>
              <a href="#" class="list-group-item">
                <h4 class="list-group-item-heading">text</h4>
                <p class="list-group-item-text">opis</p>
              </a>
            </div></div>
          <div class="form-group">
            <button type="submit" class="btn btn-primary">Snimi</button>
            <button type="submit" class="btn btn-default">Izadji</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</div>
</div>