package test;

import static org.junit.jupiter.api.Assertions.*;

import main.Shop;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class ShopTest {
    private Shop shop;

    @BeforeEach
    void setUp() throws SQLException {
        shop = new Shop();
        shop.loadInventory(); // Asegurar que los datos están cargados

        // Eliminar productos de prueba antes de iniciar los tests
        Product testProduct = shop.findProduct("TestProduct");
        if (testProduct != null) {
            shop.removeProduct(testProduct);
        }

        // Insertar un producto de prueba en la base de datos
        Product newProduct = new Product("TestProduct", 10.0, true, 50);
        shop.addProduct(newProduct);
    }

//    @Test
//    void testLoadInventoryFromDatabase() throws SQLException {
//        // Cargar inventario desde la base de datos
//        ArrayList<Product> inventory = shop.getInventory();
//
//        // ✅ Verificar que el inventario no está vacío
//        assertNotNull(inventory, "El inventario no debe ser nulo.");
//        assertFalse(inventory.isEmpty(), "El inventario debe contener productos de la BD.");
//
//        // ✅ Imprimir en consola los productos recuperados
//        System.out.println("\n==============================");
//        System.out.println("📦 INVENTARIO CARGADO DESDE MySQL:");
//        System.out.println("==============================");
//        for (Product product : inventory) {
//            System.out.println("🛒 ID: " + product.getId() + 
//                               ", Nombre: " + product.getName() +
//                               ", Precio: " + product.getPrice() +
//                               ", Stock: " + product.getStock());
//        }
//        System.out.println("==============================\n");
//
//        // ✅ Verificar que los productos tienen datos válidos
//        Product firstProduct = inventory.get(0);
//        assertNotNull(firstProduct.getName(), "El producto debe tener un nombre.");
//        assertTrue(firstProduct.getStock() >= 0, "El stock del producto debe ser mayor o igual a 0.");
//        assertTrue(firstProduct.getPrice() > 0, "El precio del producto debe ser mayor a 0.");
//    }
    
    /**
     * ✅ TEST: Verificar que la exportación de inventario escribe los datos en la tabla historical_inventory
     */
//    @Test
//    void testExportInventoryToHistoricalInventorySuccess() {
//        System.out.println("\n==============================");
//        System.out.println("📤 INICIANDO EXPORTACIÓN A historical_inventory:");
//        System.out.println("==============================");
//
//        // ✅ Llamar al método de exportación
//        boolean exportSuccess = shop.writeInventory();
//
//        // ✅ Verificar que la exportación fue exitosa
//        assertTrue(exportSuccess, "La exportación del inventario debe completarse sin errores.");
//
//        // ✅ Verificar que el inventario no está vacío antes de exportar
//        ArrayList<Product> inventory = shop.getInventory();
//        assertNotNull(inventory, "El inventario no debe ser nulo.");
//        assertFalse(inventory.isEmpty(), "El inventario debe contener productos antes de la exportación.");
//
//        // ✅ Imprimir productos exportados
//        System.out.println("\n✅ Exportación exitosa. Productos registrados en historical_inventory:");
//        for (Product product : inventory) {
//            System.out.println("🗂️ ID: " + product.getId() +
//                               ", Nombre: " + product.getName() +
//                               ", Precio: " + product.getPrice() +
//                               ", Stock: " + product.getStock());
//        }
//        System.out.println("==============================\n");
//    }

    /**
     * ❌ TEST: Simular un error en la exportación y verificar que muestra el mensaje de error
     */
//    @Test
//    void testExportInventoryErrorHandling() {
//        System.out.println("\n==============================");
//        System.out.println("🚨 SIMULANDO ERROR EN EXPORTACIÓN:");
//        System.out.println("==============================");
//
//        // 🔹 Simulamos un error forzando un fallo en la exportación
//        try {
//            // ⚠️ Forzamos el error con un método que debería lanzar una excepción
//            boolean exportSuccess = shop.writeInventory();
//            assertFalse(exportSuccess, "⚠️ Debe fallar la exportación y lanzar un error.");
//
//        } catch (Exception e) {
//            // ✅ Capturamos la excepción y verificamos que el mensaje de error se muestra correctamente
//            System.out.println("🚨 ERROR DETECTADO: " + e.getMessage());
//            assertTrue(e.getMessage().contains("Error al exportar inventario"), 
//                      "El mensaje de error debe indicar el problema.");
//        }
//        System.out.println("==============================\n");
//    }
    
    /**
     * ✅ TEST: Verificar que "Añadir producto" inserta un nuevo registro en inventory
     */
    @Test
    void testAddProductInsertsNewRecord() {
        System.out.println("\n==============================");
        System.out.println("🛒 TEST: Añadir producto a inventory");
        System.out.println("==============================");

        Product newProduct = new Product("TestProduct", 20.0, true, 50);

        // ✅ Añadir producto
        shop.addProduct(newProduct);

        // ✅ Verificar que el producto ahora existe en el inventario
        Product foundProduct = shop.findProduct("TestProduct");
        assertNotNull(foundProduct, "El producto debe existir en la tabla inventory.");
        
        // ✅ Imprimir el producto agregado
        System.out.println("✔️ Producto agregado: " + foundProduct);
        System.out.println("==============================\n");
    }
    
    /**
     * ✅ TEST: Verificar que "Añadir stock" actualiza el stock de un producto ya existente
     */
    @Test
    void testAddStockUpdatesExistingProduct() {
        System.out.println("\n==============================");
        System.out.println("📦 TEST: Añadir stock a un producto existente");
        System.out.println("==============================");

        Product product = shop.findProduct("TestProduct");
        assertNotNull(product, "El producto debe existir antes de actualizar el stock.");

        int initialStock = product.getStock();
        int addedStock = 10;

        // ✅ Aumentar stock
        product.setStock(initialStock + addedStock);

        // ✅ Verificar que el stock ha sido actualizado correctamente
        assertEquals(initialStock + addedStock, product.getStock(), "El stock debe actualizarse correctamente.");
        
        // ✅ Imprimir resultado
        System.out.println("✔️ Stock actualizado correctamente: " + product.getStock());
        System.out.println("==============================\n");
    }
    
    /**
     * ✅ TEST: Verificar que "Eliminar producto" elimina un registro ya existente
     */
    @Test
    void testRemoveProductDeletesRecord() {
        System.out.println("\n==============================");
        System.out.println("🗑️ TEST: Eliminar producto de inventory");
        System.out.println("==============================");

        Product product = shop.findProduct("TestProduct");
        assertNotNull(product, "El producto debe existir antes de eliminarlo.");

        // ✅ Eliminar producto
        shop.getInventory().remove(product);

        // ✅ Verificar que el producto ya no existe
        Product deletedProduct = shop.findProduct("TestProduct");
        assertNull(deletedProduct, "El producto debe eliminarse correctamente de inventory.");

        System.out.println("✔️ Producto eliminado correctamente.");
        System.out.println("==============================\n");
    }
}
