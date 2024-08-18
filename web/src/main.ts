import {
  createProcessor,
  numberOfProcessors,
  renderCache,
  renderMemory,
  renderOutput
} from "./api";

window.addEventListener("load", async () => {
  renderMemory();

  var layout = "";
  for (var i = 0; i < numberOfProcessors; i++) {
    var processorId = await createProcessor();
    renderCache(processorId);
    renderOutput(processorId);
    layout += `"output-${processorId} cache-${processorId} memory"\n`
  }

  var app = document.querySelector(".app") as HTMLDivElement;
  app.style.gridTemplateAreas = layout;
})
