// LINE CHART EXAMPLE

var MONTHS = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

function getMonths(count) {
  var values = [];

  for (i = 0; i < count; ++i) {
    values.push(MONTHS[i]);
  }

  return values;
}

var myLineChart = document.getElementById("myLineChart");

var lineChartConfig = {
  type: "line",
  data: {
    labels: getMonths(7),
    datasets: [
      {
        label: "My First Dataset",
        data: [65, 59, 80, 81, 56, 55, 40],
        fill: false,
        borderColor: "rgb(75, 192, 192)",
        tension: 0,
      },
    ],
  },
};

var lineChart = new Chart(myLineChart, lineChartConfig);
