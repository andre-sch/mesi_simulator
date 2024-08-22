import axios from "axios";
import { MemoryRenderer } from "./memory";
import { CacheRenderer, Line } from "./cache";
import { Room, OutputRenderer } from "./output";

var api = axios.create({
  baseURL: "http://localhost:8080"
});

var processorIds = [0, 1, 2];
var response = await api.get<{ count: number }>(`/rooms/count`);
var roomCount = response.data.count;

async function renderInterface() {
  renderMemory();

  processorIds.forEach(id => {
    renderCache(id);
    renderOutput(id);
  });
}

async function selectRoom(roomId: number, processorId: number): Promise<void> {
  var response = await api.put<Room>(`/rooms/${roomId}`, { processorId });
  var room = response.data;

  renderRoomUpdate(room, processorId);
}

async function reserveSeat(roomId: number, seatId: number, processorId: number): Promise<void> {
  var response = await api.put<Room>(`/rooms/${roomId}/seats/${seatId}/reserve`, { processorId });
  var room = response.data;

  renderRoomUpdate(room, processorId);
}

function renderRoomUpdate(room: Room, processorId: number): void {
  renderMemory();
  processorIds.forEach(id => renderCache(id));
  renderOutput(processorId, room);
}

async function renderMemory(): Promise<void> {
  var memoryRenderer = new MemoryRenderer();
  var content = await getMemoryContent();
  memoryRenderer.render(content);
}

async function getMemoryContent(): Promise<number[]> {
  var response = await api.get<number[]>("/shared-memory");
  return response.data;
}

async function renderCache(id: number): Promise<void> {
  var cacheRenderer = new CacheRenderer(id);
  var content = await getCacheContent(id);
  cacheRenderer.render(content);
}

async function getCacheContent(id: number): Promise<Line[]> {
  var response = await api.get<Line[]>(`/caches/${id}`);
  return response.data;
}

async function renderOutput(processorId: number, newContent?: Room) {
  var outputRenderer = new OutputRenderer(processorId, roomCount);
  outputRenderer.render(newContent);
}

export {
  renderInterface,
  selectRoom ,
  reserveSeat
};