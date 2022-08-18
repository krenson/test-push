// BAR CHART EXAMPLE

var myBarChart = document.getElementById("myBarChart");

var barChartConfig = {
  type: "bar",
  data: {
    labels: [
      "Temps partiel",
      "temps plein a p...",
      "temps plein jour",
      "autres ou inconnu",
    ],
    datasets: [
      {
        label: "Agent d'entretien des parcs et jardins",
        data: [90, 40, 170, 40],
        fill: false,
        backgroundColor: "rgb(255, 142, 151)",
        borderColor: "rgb(255, 142, 151)",
      },
      {
        label: "Tous les métiers",
        data: [100, 30, 160, 80],
        fill: false,
        backgroundColor: "rgb(251, 231, 203)",
        borderColor: "rgb(251, 231, 203)",
      },
    ],
  },
  options: {
    scales: {
      y: {
        max: 300,
        beginAtZero: true,
        ticks: {
          stepSize: 100,
        },
      },
    },
  },
};

var barChart = new Chart(myBarChart, barChartConfig);
