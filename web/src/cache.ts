import { wrapperOf } from "./wrapper";

export interface Line {
  blockNumber: number;
  status: string;
  content: number[];
}

export class CacheRenderer {
  private container = document.querySelector(".app") as Element;
  private className: string;

  constructor(id: number) {
    this.className = `processor-${id}`;
  }

  private cacheQuery(): string { return `.cache.${this.className}`; }

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
    var cache = this.cache();
    cache.appendChild(this.header());
  
    data.forEach((line, index) => {
      cache.appendChild(this.row(line, index));
    })

    this.container.appendChild(wrapperOf(cache));
  }

  private cache(): Element {
    var cache = document.createElement("table");
    cache.classList.add("cache", this.className);
    return cache;
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
