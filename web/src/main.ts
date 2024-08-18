import * as api from "./api";

window.addEventListener("load", async () => {
  api.renderMemory();

  var numberOfProcessors = 3;
  for (var i = 0; i < numberOfProcessors; i++) {
    var processorId = await api.createProcessor();
    api.renderCache(processorId);
    api.renderOutput(processorId);
  }
})
