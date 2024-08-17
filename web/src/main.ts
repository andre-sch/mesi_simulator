import * as api from "./api";

window.addEventListener("load", async () => {
  api.renderMemory();

  var computerId = await api.createProcessor();

  api.renderCache(computerId);
  api.renderOutput(computerId);
})
