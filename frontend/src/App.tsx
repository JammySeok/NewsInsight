// 1. 라우팅 관련 컴포넌트 임포트
import { Routes, Route, BrowserRouter } from 'react-router-dom';

// 2. 페이지 컴포넌트 임포트
import IndexPage from './pages/IndexPage';
import LoginPage from './pages/LoginPage';

// 3. 기본 CSS 파일 임포트 (기존 App.css는 지워도 됩니다)
// import './App.css' 

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<IndexPage />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;