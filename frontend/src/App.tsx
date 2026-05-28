import { Route, Routes } from 'react-router-dom';
import BrowsePage from './pages/BrowsePage';
import WatchPage from './pages/WatchPage';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<BrowsePage />} />
      <Route path="/watch/:titleId" element={<WatchPage />} />
    </Routes>
  );
}
