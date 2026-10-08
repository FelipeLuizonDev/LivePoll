import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Enquetes from './pages/Enquetes'
import Home from './pages/Home'
import Header from './components/Header'

function App() {
  return (
    <BrowserRouter>
      <Header />
      
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/enquetes" element={<Enquetes />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App