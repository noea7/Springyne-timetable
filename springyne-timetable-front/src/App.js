import { HashRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import HomePage from './pages/Home';
import Navigation from './components/Navigation';
import ModuleListPage from './pages/ModuleList';
import RoomListPage from './pages/RoomList';
import ShiftListPage from './pages/ShiftList';
import SubjectListPage from './pages/SubjectList';
import TeacherListPage from './pages/TeacherList';
import CreateModulePage from './pages/CreateModule';
import ViewModulePage from './pages/ViewModule';
import EditModulePage from './pages/EditModule';
import CreateTeacherPage from './pages/CreateTeacher';
import ViewTeacherPage from './pages/ViewTeacher';
import EditTeacherPage from './pages/EditTeacher';

function App() {
  return (
    <div className="App">
      <HashRouter>
        <Navigation />
        <div className="container-xxl">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/modules" element={<ModuleListPage />} />
            <Route path="/rooms" element={<RoomListPage />} />
            <Route path="/shifts" element={<ShiftListPage />} />
            <Route path="/subjects" element={<SubjectListPage />} />
            <Route path="/teachers" element={<TeacherListPage />} />
            <Route path="/modules/create" element={<CreateModulePage />} />
            <Route path="/modules/view/:id" element={<ViewModulePage />} />
            <Route path="/modules/edit/:id" element={<EditModulePage />} />
            <Route path="/teachers/create" element={<CreateTeacherPage />} />
            <Route path="/teachers/view/:id" element={<ViewTeacherPage />} />
            <Route path="/teachers/edit/:id" element={<EditTeacherPage />} />
          </Routes>
        </div>
      </HashRouter>
    </div>
  );
}

export default App;
