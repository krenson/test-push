// LINE CHART EXAMPLE
var COLORS = [
  "rgb(47, 58, 72)",
  "rgb(251, 231, 203)",
  "rgb(255, 142, 151)",
  "rgb(255, 255, 255)",
  "rgb(222, 199, 224)",
  "rgb(150, 156, 163)",
  "rgb(252, 219, 173)",
];

var COLORBORDERS = [
  "rgb(47, 58, 72)",
  "rgb(251, 231, 203)",
  "rgb(255, 142, 151)",
  "rgb(54, 64, 75)",
  "rgb(222, 199, 224)",
  "rgb(150, 156, 163)",
  "rgb(54, 64, 75)",
];

var MONTHS = [
  "janvier",
  "fevrier",
  "mars",
  "avril",
  "mai",
  "juin",
  "julliet",
  "août",
  "septembre",
  "octobre",
  "novembre",
  "decembre",
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
        lineTension: 0.3,
        pointRadius: 5,
        pointHoverRadius: 5,
        pointHitRadius: 40,
        label: "My First Dataset",
        data: [65, 59, 80, 81, 56, 55, 40],
        fill: false,
        backgroundColor: COLORS[0],
        borderColor: COLORBORDERS[0],
        tension: 0,
      },
    ],
  },
  options: {
    plugins: {
      legend: {
        labels: {
          font: {
            family: "Poppins",
          },
        },
      },
    },

    scales: {
      y: {
        beginAtZero: true,
        grid: {
          color: "rgb(246, 241, 234)",
          lineWidth: 2,
          borderColor: "rgb(219, 202, 175)",
          borderWidth: 3,
          borderRadius: "15px",
        },
        ticks: {
          stepSize: 20,
          font: {
            size: 15,
            family: "Poppins",
          },
        },
      },
      x: {
        grid: {
          color: "rgb(246, 241, 234)",
          lineWidth: 2,
          borderColor: "rgb(219, 202, 175)",
          borderWidth: 3,
        },
        ticks: {
          font: {
            size: 15,
            family: "Poppins",
          },
        },
      },
    },
  },
};

var lineChart = new Chart(myLineChart, lineChartConfig);
Chart.defaults.font.family = "Poppins";
Chart.defaults.font.size = 15;

// Chart details

var detailsBtn = document.getElementById(
  "accordion-2098845c33-item-ace86c638b-button"
);

var detailsCloseBtn = document.getElementById("close-container");

detailsCloseBtn.addEventListener("click", () => {
  detailsBtn.click();
});
