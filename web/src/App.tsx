import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Enquetes from './pages/Enquetes'
import Home from './pages/Home'
import Header from './components/Header'
import CriarEnquete from './pages/CriarEnquete'

function App() {
  return (
    <BrowserRouter>
      <Header />
      
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/enquetes" element={<Enquetes />} />
        <Route path="/enquetes/nova" element={<CriarEnquete />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App