var timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
var locale = Intl.DateTimeFormat().resolvedOptions().locale;

function formatDates() {
	$(".table-column-date").each(function() {
		var currValue = $(this).html();
		if (currValue) {
			var formattedValue = formatAsLocalDate(currValue);
			$(this).html(formattedValue);
		}
	});
}

function formatAsLocalDate(utcDate) {
	var date = new Date(utcDate);
	date.toLocaleString(locale, { timeZone: timeZone })
	console.log("local date: " + date);
	return formatDate(date);
}

function formatDate(date_ob) {
	let date = ("0" + date_ob.getDate()).slice(-2);
	let month = ("0" + (date_ob.getMonth() + 1)).slice(-2);
	let year = date_ob.getFullYear();
	let hours = date_ob.getHours();
	let minutes = date_ob.getMinutes();
	return year + "-" + month + "-" + date + " " + hours + ":" + minutes;


}

function getCurrPageSuffix() {
	var pathName = window.location.pathname;
	if (pathName.includes("/")) {
		return pathName.substring(pathName.lastIndexOf("/"));
	}
}

		
