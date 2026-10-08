import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Enquetes from './pages/Enquetes'
import Home from './pages/Home'
import Header from './components/Header'
import CriarEnquete from './pages/CriarEnquete'
import Resultados from './pages/Resultados'

function App() {
  return (
    <BrowserRouter>
      <Header />
      
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/enquetes" element={<Enquetes />} />
        <Route path="/enquetes/nova" element={<CriarEnquete />} />
        <Route path="/resultados" element={<Resultados />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App