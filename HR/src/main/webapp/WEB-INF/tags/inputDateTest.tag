<div id="sandbox-container">
    <label for="testdate">Test Date</label>

    <div class="input-group date">
        <input id="testdate" type="text" class="form-control">
        <span class="input-group-addon">
            <i class="glyphicon glyphicon-th"></i>
        </span>
    </div>
</div>
<script type="text/javascript">
    $('#sandbox-container .input-group.date').datepicker({
        format: "dd.mm.yyyy",
        language: "sr-latin",
        calendarWeeks: true,
        autoclose: true,
        todayHighlight: true,
        beforeShowDay: function (date) {
            if (date.getMonth() == (new Date()).getMonth())
                switch (date.getDate()) {
                    case 4:
                        return {
                            tooltip: 'Example tooltip',
                            classes: 'active'
                        };
                    case 8:
                        return false;
                    case 12:
                        return "green";
                }
        },
        beforeShowMonth: function (date) {
            switch (date.getMonth()) {
                case 8:
                    return false;
            }
        },
        datesDisabled: ['06/06/2015', '06/21/2015'],
        toggleActive: true,
        defaultViewDate: {year: 1977, month: 04, day: 25}
    });
</script>