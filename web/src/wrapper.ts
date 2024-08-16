function wrapperOf(node: Element): Element {
  var wrapper = document.createElement("div");

  wrapper.classList.add("wrapper");
  wrapper.appendChild(node);

  return wrapper;
}

export { wrapperOf };