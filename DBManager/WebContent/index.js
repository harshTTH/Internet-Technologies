let props = {
  rowCount: 1,
  colCount: 1,
  colNames: ""
};
const html = function(select, ...args) {
  switch (select) {
    case 0:
      return `<tr id="row${args[0]}">
						<td>
							<input class="form-control" type="text" name="colName" required>
						</td>
						<td>
							<select class="custom-select" id="colType" name="colType">
								<option selected>Choose...</option>
								<option value="Int">Int</option>
								<option value="Varchar">Varchar</option>
		 					</select>
						</td>
						<td>
							<input class="form-control" type="text" name="colSize" value="">
						</td>
						<td>
							<input type="text" plaaceholder="[Constraints]" name="primary">
						</td>
					</tr>`;
  }
};

$("#insertTabForm").on("show.bs.modal", () => {
  $.ajax({
    url: "/DBManager/fetchTables",
    type: "get",
    success: function(result) {
      let tables = result.split("-");
      tables.forEach(tableName => {
        $("#tablesMenu").append(
          `<a class="dropdown-item tab" href="#">${tableName}</a>`
        );
      });
    }
  });
});

$(document).on("click", ".tab", event => {
  $("#tableName").attr("value", event.target.innerText);
  $("#tabHead").empty();
  $("#tabBody").empty();
  let tableName = event.target.innerText;
  $.ajax({
    url: "/DBManager/fetchTableData",
    type: "post",
    data: {
      tableName
    },
    success: function(result) {
      const colNames = result.split("|");
      props.colNames = colNames;
      colNames.forEach(name => {
        $("#tabHead").append(`<td>${name}</td>`);
      });
      $("#tabBody").append(`<tr id='tabRow${props.rowCount}'></tr>`);
      colNames.forEach(name => {
        $(document)
          .find(`#tabRow${props.rowCount}`)
          .append(
            '<td><input class="form-control" type="text" name="row" required></td></tr>'
          );
      });
      props.rowCount++;
    }
  });
});

$("#addCol").click(() => {
  props.colCount++;
  $("#crtTableBody").append(html(0, props.colCount));
});

$("#rmCol").click(() => {
  $(`#row${props.colCount}`).remove();
  props.colCount--;
});

$("#addRow").click(() => {
  $("#tabBody").append(`<tr id='tabRow${props.rowCount}'></tr>`);
  props.colNames.forEach(name => {
    $(document)
      .find(`#tabRow${props.rowCount}`)
      .append(
        '<td><input class="form-control" type="text" name="row" required></td></tr>'
      );
  });
  props.rowCount++;
});
