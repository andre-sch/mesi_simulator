import axios from "axios";
import { MemoryRenderer } from "./memory";
import { CacheRenderer, Line } from "./cache";
import { Room, RoomRenderer } from "./room";

var api = axios.create({
  baseURL: "http://localhost:8080"
});

async function renderMemory(): Promise<void> {
  var memoryRenderer = new MemoryRenderer();
  var content = await getMemoryContent();
  memoryRenderer.render(content);
}

async function renderCache(id: number): Promise<void> {
  var cacheRenderer = new CacheRenderer(id);
  var content = await getCacheContent(id);
  cacheRenderer.render(content);
}

async function createProcessor(): Promise<number> {
  var response = await api.post<{ id: number }>("/processors");
  return response.data.id;
}

async function selectRoom(roomId: number, processorId: number): Promise<void> {
  var response = await api.put<Room>(`/rooms/${roomId}`, { processorId });
  var roomRenderer = new RoomRenderer(processorId);
  roomRenderer.render(response.data);
  renderCache(processorId);
  renderMemory();
}

async function reserveSeat(roomId: number, seatId: number, processorId: number): Promise<void> {
  await api.put(`/rooms/${roomId}/seats/${seatId}/reserve`, { processorId });
  selectRoom(roomId, processorId);
}

async function getCacheContent(id: number): Promise<Line[]> {
  return (await api.get<Line[]>(`/caches/${id}`)).data;
}

async function getMemoryContent(): Promise<number[]> {
  return (await api.get<number[]>("/shared-memory")).data;
}

export { renderMemory, renderCache, createProcessor, selectRoom, reserveSeat };