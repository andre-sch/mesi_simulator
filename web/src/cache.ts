import { wrapperOf } from "./wrapper";

export interface Line {
  blockNumber: number;
  status: string;
  content: number[];
}

export class CacheRenderer {
  private container = document.querySelector(".app") as Element;
  private processorName: string;

  constructor(id: number) {
    this.processorName = `processor-${id}`;
  }

  private cacheQuery(): string { return `.cache.${this.processorName}`; }

  public render(data: Line[]) {
    var alreadyExists = document.querySelector(this.cacheQuery()) != null;
    if (alreadyExists) this.update(data);
    else this.initialize(data);
  }

  private update(newData: Line[]) {
    var dataRows = document.querySelectorAll(`${this.cacheQuery()} tr:has(td)`);

    dataRows.forEach((row, index) => {
      row.replaceWith(this.row(newData[index], index));
    })
  }

  private initialize(data: Line[]) {
    var table = document.createElement("table");
    table.appendChild(this.header());
  
    data.forEach((line, index) => {
      table.appendChild(this.row(line, index));
    })

    var wrapper = wrapperOf(table);
    wrapper.classList.add("cache", this.processorName);
    this.container.appendChild(wrapper);
  }

  private header(): Element {
    var header = document.createElement("tr");
    
    header.innerHTML = `
      <th>Índice</th>
      <th>Bloco</th>
      <th>Status</th>
      <th>Conteúdo</th>
    `;

    return header;
  }

  private row(line: Line, index: number): Element {
    var row = document.createElement("tr");
  
    row.innerHTML += `
      <td>${index}</td>
      <td>${line.blockNumber}</td>
      <td>${line.status.charAt(0)}</td>
      <td>${line.content.join(", ")}</td>
    `;

    return row;
  }
}
