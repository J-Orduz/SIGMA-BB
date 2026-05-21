import { useState, useEffect } from 'react';
import type { Brand, CreateBrandDTO } from '../types/brand.types';
import { brandService } from '../services/brand.service';

export const useBrands = () => {
  const [brands, setBrands] = useState<Brand[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchBrands = async () => {
    setIsLoading(true);
    try {
      const data = await brandService.getAll();
      setBrands(data);
    } catch (err) {
      setError('No se pudieron cargar las marcas. Compruebe la conexión con el servidor.');
    } finally {
      setIsLoading(false);
    }
  };

  const createBrand = async (brandData: CreateBrandDTO) => {
    setIsLoading(true);
    setError(null);
    try {
      await brandService.create(brandData);
      await fetchBrands();
    } catch (err) {
      setError('Error al registrar la marca. Asegúrese de que no esté duplicada.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateBrand = async (id: string, newName: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await brandService.update(id, newName);
      await fetchBrands(); // Recarga la lista actualizada
    } catch (err) {
      setError('No se pudo cambiar el nombre de la marca. Intente nuevamente.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteBrand = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      await brandService.delete(id);
      await fetchBrands(); // Recarga la lista sin el elemento eliminado
    } catch (err) {
      setError('No es posible eliminar esta marca porque ya se encuentra asignada a equipos del sistema.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchBrands();
  }, []);

  return {
    brands,
    isLoading,
    error,
    createBrand,
    updateBrand,
    deleteBrand,
    setError
  };
};